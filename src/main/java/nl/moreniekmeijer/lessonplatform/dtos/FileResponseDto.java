package nl.moreniekmeijer.lessonplatform.dtos;

import nl.moreniekmeijer.lessonplatform.models.FileType;

public class FileResponseDto {

    private String fileName;
    private String filePath;
    private String mimeType;
    private FileType fileType;

    public FileResponseDto(String fileName, String filePath, String mimeType, FileType fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
