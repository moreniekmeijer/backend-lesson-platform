package nl.moreniekmeijer.lessonplatform.dtos;

import nl.moreniekmeijer.lessonplatform.models.FileType;

public class FileResponseDto {

    private String objectName;
//    private String signedUrl;
    private String mimeType;
    private FileType fileType;

    public FileResponseDto(String objectName, String mimeType, FileType fileType) {
        this.objectName = objectName;
//        this.signedUrl = signedUrl;
        this.mimeType = mimeType;
        this.fileType = fileType;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
