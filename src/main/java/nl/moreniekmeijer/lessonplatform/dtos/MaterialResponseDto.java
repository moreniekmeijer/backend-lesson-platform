package nl.moreniekmeijer.lessonplatform.dtos;

public class MaterialResponseDto {

    private Long id;
    private String title;
    private String fileType;
    private String filePath;
    private String link;
    private String instrument;
    private String styleName;

    public MaterialResponseDto() {
    }

    public MaterialResponseDto(Long id, String title, String fileType, String filePath, String link, String instrument, String styleName) {
        this.id = id;
        this.title = title;
        this.fileType = fileType;
        this.filePath = filePath;
        this.link = link;
        this.instrument = instrument;
        this.styleName = styleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
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

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}
