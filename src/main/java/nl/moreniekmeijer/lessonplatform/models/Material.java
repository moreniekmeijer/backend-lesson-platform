package nl.moreniekmeijer.lessonplatform.models;

import jakarta.persistence.*;

@Entity
@Table
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private String filePath;
    private String link;
    private String instrument;

    @ManyToOne
    @JoinColumn(name = "styles_id")
    private Style style;

    public Material() {
    }

    public Material(Long id, String title, FileType fileType, String filePath, String link, String instrument, Style style) {
        this.id = id;
        this.title = title;
        this.fileType = fileType;
        this.filePath = filePath;
        this.link = link;
        this.instrument = instrument;
        this.style = style;
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

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
