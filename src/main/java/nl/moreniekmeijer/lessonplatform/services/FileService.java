package nl.moreniekmeijer.lessonplatform.services;

import nl.moreniekmeijer.lessonplatform.dtos.FileResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class FileService {
    private final Path fileStoragePath;

    public FileService(@Value("${file.upload-location}") Path fileStoragePath) throws IOException {
        this.fileStoragePath = fileStoragePath.toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (IOException e) {
            throw new IOException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public FileResponseDto saveFile(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        String mimeType = getMimeType(file, extension);

        // Bepaal het FileType op basis van de extensie
        FileType fileType = switch (extension) {
            case "pdf" -> FileType.PDF;
            case "mp4", "mov" -> FileType.VIDEO;
            case "mp3" -> FileType.AUDIO;
            default -> throw new IllegalArgumentException("Unsupported file type.");
        };

        // Bestanden opslaan
        Path filePath = this.fileStoragePath.resolve(originalFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return new FileResponseDto(
                originalFilename,
                filePath.toString(),
                mimeType,
                fileType
        );
    }

    public Resource downloadFile(String fileName) {
        Path path = this.fileStoragePath.resolve(fileName);
        Resource resource;

        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("issue in reading file", e);
        }

        if(resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read file " + fileName);
        }
    }

//    TODO - nutteloze methode moet weg
    public FileResponseDto saveLink(String link) {
        // Als er een link is, verwerk dan de link
        return new FileResponseDto(
                "Link", // Naam voor de link
                link,
                "application/link", // Dit is een placeholder voor MIME type
                FileType.LINK
        );
    }

    private String getMimeType(MultipartFile file, String extension) {
        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "mp4" -> "video/mp4";
            case "mp3" -> "audio/mpeg";
            default -> "application/octet-stream"; // fallback MIME-type
        };
    }
}
