package nl.moreniekmeijer.lessonplatform.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
public class FileService {

    private final Storage storage;
    private final String bucketName;

    public FileService(
            @Value("${gcs.bucket-name}") String bucketName
//            @Value("${gcs.credentials.file:}") String credentialsPath
    ) throws IOException {
        this.bucketName = bucketName;
        this.storage = StorageOptions.getDefaultInstance().getService();

//        StorageOptions.Builder optionsBuilder = StorageOptions.newBuilder();
//
//        if (credentialsPath != null && !credentialsPath.isBlank()) {
//            optionsBuilder.setCredentials(
//                    ServiceAccountCredentials.fromStream(new FileInputStream(credentialsPath))
//            );
//        } else {
//            optionsBuilder.setCredentials(GoogleCredentials.getApplicationDefault());
//        }
//
//        this.storage = optionsBuilder.build().getService();
    }

    public Storage getStorage() {
        return storage;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void uploadFile(File file, String objectName, String contentType) throws IOException {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName))
                .setContentType(contentType)
                .build();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }

    public File convertMovToMp4(File movFile) throws IOException {
        File outputFile = File.createTempFile("converted_", ".mp4");
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-i", movFile.getAbsolutePath(),
                "-vcodec", "libx264",
                "-preset", "ultrafast",
                "-crf", "28",
                "-vf", "scale=720:-2",
                "-pix_fmt", "yuv420p",
                "-movflags", "+faststart",
                "-c:a", "aac",
                "-ar", "44100",
                "-ac", "2",
                "-b:a", "128k",
                "-threads", String.valueOf(Runtime.getRuntime().availableProcessors()),
                "-loglevel", "error",
                outputFile.getAbsolutePath()
        );

        pb.inheritIO();
        try {
            int exitCode = pb.start().waitFor();
            if (exitCode != 0) throw new RuntimeException("ffmpeg conversion failed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("ffmpeg interrupted", e);
        }
        return outputFile;
    }

    public String generateSignedUploadUrl(String objectName, String contentType) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName))
                .setContentType(contentType)
                .build();
        URL signedUrl = storage.signUrl(
                blobInfo,
                15, TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.withContentType()
        );
        return signedUrl.toString();
    }

    public String generateSignedUrl(String objectName, boolean download, String materialTitle) {
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob == null) throw new RuntimeException("File not found: " + objectName);

        String extension = getFileExtension(objectName);
        String safeTitle = (materialTitle != null) ? materialTitle.replaceAll("[^a-zA-Z0-9-_]", "_") : objectName;
        String finalFileName = safeTitle + "." + extension;
        String disposition = download ? "attachment; filename=\"" + finalFileName + "\""
                : "inline; filename=\"" + finalFileName + "\"";
        Map<String, String> queryParams = Map.of("response-content-disposition", disposition);

        return blob.signUrl(
                7, TimeUnit.DAYS,
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withQueryParams(queryParams)
        ).toString();
    }

    public void deleteFile(String filePath) {
        try {
            Bucket bucket = storage.get(bucketName);
            Blob blob = bucket.get(filePath);
            if (blob != null && blob.exists()) {
                blob.delete();
            } else {
                throw new RuntimeException("File not found in bucket: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from GCS: " + e.getMessage(), e);
        }
    }

    /* ------------------- Helpers ------------------- */
    public String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot > 0) ? filename.substring(lastDot + 1) : "";
    }

    public FileType getFileTypeFromFilename(String filename) {
        String ext = getFileExtension(filename).toLowerCase();
        return switch (ext) {
            case "pdf" -> FileType.PDF;
            case "mp4", "mov" -> FileType.VIDEO;
            case "jpeg", "jpg", "png" -> FileType.IMAGE;
            default -> throw new IllegalArgumentException("Unsupported file type: " + ext);
        };
    }

//    public String getMimeTypeFromFilename(String filename) {
//        String ext = getFileExtension(filename).toLowerCase();
//        return switch (ext) {
//            case "pdf" -> "application/pdf";
//            case "mp4" -> "video/mp4";
//            case "mov" -> "video/quicktime";
//            case "jpg", "jpeg" -> "image/jpeg";
//            case "png" -> "image/png";
//            default -> "application/octet-stream";
//        };
//    }
}