package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.moreniekmeijer.lessonplatform.models.FileType;

public class MaterialInputDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "FileType is required")
    private FileType fileType;

    private String filePath;

    private String link;

    @NotBlank(message = "Instrument is required")
    private String instrument;

    private String category;

    private Long styleId;

    public MaterialInputDto() {
    }

    public MaterialInputDto(String title, FileType fileType, String filePath, String link, String instrument, String category, Long styleId) {
        this.title = title;
        this.fileType = fileType;
        this.filePath = filePath;
        this.link = link;
        this.instrument = instrument;
        this.category = category;
        this.styleId = styleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }
}
