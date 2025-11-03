package nl.moreniekmeijer.lessonplatform.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import nl.moreniekmeijer.lessonplatform.dtos.FileResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {

    private final Storage storage;
    private final String bucketName;

    public FileService(
            @Value("${gcs.bucket-name}") String bucketName,
            @Value("${gcs.credentials.file:}") String credentialsPath
    ) throws IOException {
        this.bucketName = bucketName;

        StorageOptions.Builder optionsBuilder = StorageOptions.newBuilder();

        if (credentialsPath != null && !credentialsPath.isBlank()) {
            optionsBuilder.setCredentials(
                    ServiceAccountCredentials.fromStream(new FileInputStream(credentialsPath))
            );
        } else {
            optionsBuilder.setCredentials(GoogleCredentials.getApplicationDefault());
        }

        this.storage = optionsBuilder.build().getService();
    }

    public FileResponseDto saveFile(MultipartFile file, String materialTitle) throws IOException {
        File tempFile = convertMultipartToFile(file);
        String originalFilename = cleanFileName(file.getOriginalFilename());
        String extension = getFileExtension(originalFilename).toLowerCase();

        FileType fileType = determineFileType(extension);

        if ("mov".equals(extension)) {
            File converted = convertMovToMp4(tempFile);
            tempFile.delete();
            tempFile = converted;
            extension = "mp4";
            fileType = FileType.VIDEO;
        }

        String uniqueFileName = generateUniqueFileName(materialTitle, extension);

        uploadToGcs(tempFile, uniqueFileName, getMimeType(extension));
        tempFile.delete();

        return new FileResponseDto(uniqueFileName, getMimeType(extension), fileType);
    }

    public String generateSignedUrl(String objectName, boolean download, String materialTitle) {
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob == null) throw new RuntimeException("File not found in bucket: " + objectName);

        String extension = getFileExtension(objectName);
        String safeTitle = (materialTitle != null) ? materialTitle.replaceAll("[^a-zA-Z0-9-_]", "_") : objectName;
        String finalFileName = safeTitle + "." + extension;

        String disposition = download
                ? "attachment; filename=\"" + finalFileName + "\""
                : "inline; filename=\"" + finalFileName + "\"";

        Map<String, String> queryParams = Map.of("response-content-disposition", disposition);

        URL signedUrl = blob.signUrl(
                7, TimeUnit.DAYS,
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withQueryParams(queryParams)
        );

        return signedUrl.toString();
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

    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("upload_", null);
        file.transferTo(convFile);
        return convFile;
    }

    private String cleanFileName(String originalFilename) {
        return StringUtils.cleanPath(Objects.requireNonNull(originalFilename));
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot > 0) ? filename.substring(lastDot + 1) : "";
    }

    private FileType determineFileType(String extension) {
        return switch (extension) {
            case "pdf" -> FileType.PDF;
            case "mp4", "mov" -> FileType.VIDEO;
            case "jpeg", "jpg", "png" -> FileType.IMAGE;
            default -> throw new IllegalArgumentException("Unsupported file type: " + extension);
        };
    }

    private String generateUniqueFileName(String title, String extension) {
        String safeTitle = (title != null)
                ? title.replaceAll("[^a-zA-Z0-9-_]", "_")
                : UUID.randomUUID().toString();
        return safeTitle + "." + extension;
    }

    private void uploadToGcs(File file, String objectName, String mimeType) throws IOException {
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(mimeType)
                .build();
        storage.create(blobInfo, java.nio.file.Files.readAllBytes(file.toPath()));
    }

    private File convertMovToMp4(File movFile) throws IOException {
        File outputFile = File.createTempFile("converted_", ".mp4");
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-i", movFile.getAbsolutePath(),
                "-vcodec", "libx264",
                "-crf", "28",
                "-preset", "fast",
                "-vf", "scale=720:-2",
                "-acodec", "aac",
                "-b:a", "128k",
                outputFile.getAbsolutePath()
        );
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("ffmpeg conversion failed with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("ffmpeg conversion interrupted", e);
        }
        return outputFile;
    }

    private String getMimeType(String extension) {
        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "mp4" -> "video/mp4";
            case "mov" -> "video/quicktime";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            default -> "application/octet-stream";
        };
    }
}
