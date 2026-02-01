package nl.moreniekmeijer.lessonplatform.utils;

import nl.moreniekmeijer.lessonplatform.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileServiceHolder {

    private static FileService fileService;

    @Autowired
    public FileServiceHolder(FileService fileService) {
        FileServiceHolder.fileService = fileService;
    }

    public static String generateSignedUrl(String fileName, boolean download, String title) {
        return fileService.generateSignedUrl(fileName, download, title);
    }
}