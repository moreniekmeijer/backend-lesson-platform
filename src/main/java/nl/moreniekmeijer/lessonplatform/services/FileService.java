package nl.moreniekmeijer.lessonplatform.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import nl.moreniekmeijer.lessonplatform.dtos.FileResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

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

    public FileResponseDto saveFile(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        String mimeType = getMimeType(extension);

        FileType fileType = switch (extension) {
            case "pdf" -> FileType.PDF;
            case "mp4", "mov" -> FileType.VIDEO;
            case "jpeg", "jpg", "png" -> FileType.IMAGE;
            default -> throw new IllegalArgumentException("Unsupported file type: " + extension);
        };

        String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;

        // Upload file naar GCS
        BlobId blobId = BlobId.of(bucketName, uniqueFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(mimeType)
                .build();
        storage.create(blobInfo, file.getBytes());

        // Optioneel: public link maken
//        URL signedUrl = storage.signUrl(blobInfo, 7, java.util.concurrent.TimeUnit.DAYS);

        return new FileResponseDto(
                uniqueFileName,
//                signedUrl.toString(),
                mimeType,
                fileType
        );
    }

    public Resource downloadFile(String objectName) {
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob == null) {
            throw new RuntimeException("File not found in bucket: " + objectName);
        }

        URL signedUrl = blob.signUrl(7, java.util.concurrent.TimeUnit.DAYS);

        try {
            return new UrlResource(signedUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create resource from signed URL", e);
        }
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
