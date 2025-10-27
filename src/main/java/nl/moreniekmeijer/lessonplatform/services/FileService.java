package nl.moreniekmeijer.lessonplatform.services;

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
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Service
public class FileService {

    private final Storage storage;
    private final String bucketName;

    public FileService(
            @Value("${gcs.bucket-name}") String bucketName,
            @Value("${gcs.credentials}") String credentialsJson
    ) throws IOException {
        this.bucketName = bucketName;

        this.storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new ByteArrayInputStream(credentialsJson.getBytes())
                ))
                .build()
                .getService();
    }

    public FileResponseDto saveFile(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        String mimeType = getMimeType(extension);

        FileType fileType = switch (extension) {
            case "pdf" -> FileType.PDF;
            case "mp4", "mov" -> FileType.VIDEO;
            case "jpeg", "jpg", "png" -> FileType.IMAGE;
            default -> throw new IllegalArgumentException("Unsupported file type.");
        };

        // Upload file naar GCS
        BlobId blobId = BlobId.of(bucketName, originalFilename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(mimeType)
                .build();
        storage.create(blobInfo, file.getBytes());

        // Optioneel: public link maken
        URL signedUrl = storage.signUrl(blobInfo, 7, java.util.concurrent.TimeUnit.DAYS);

        return new FileResponseDto(
                originalFilename,
                signedUrl.toString(),
                mimeType,
                fileType
        );
    }

    public Resource downloadFile(String fileName) {
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        if (blob == null) {
            throw new RuntimeException("File not found in bucket: " + fileName);
        }

        URL signedUrl = blob.signUrl(7, java.util.concurrent.TimeUnit.DAYS);
        return new UrlResource(signedUrl);
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
