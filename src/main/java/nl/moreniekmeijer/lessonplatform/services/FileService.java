package nl.moreniekmeijer.lessonplatform.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    public String saveFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = this.fileStoragePath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

}
