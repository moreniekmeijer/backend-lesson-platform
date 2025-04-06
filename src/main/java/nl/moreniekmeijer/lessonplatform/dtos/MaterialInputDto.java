package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotBlank;
import nl.moreniekmeijer.lessonplatform.models.FileType;

public class MaterialInputDto {

    @NotBlank(message = "Title is required")
    private String title;

//    TODO - Fix in frontend
//    @NotNull(message = "FileType is required")
    private FileType fileType;

    private String filePath;

    private String instrument;

    private String category;

    private Long styleId;

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
