package nl.moreniekmeijer.lessonplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
    private String fileName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "material_instruments",
            joinColumns = @JoinColumn(name = "material_id")
    )
    @Column(name = "instrument")
    private Set<String> instruments = new HashSet<>();
    private String category;

    @ManyToOne
    @JoinColumn(name = "styles_id")
    @JsonIgnore
    private Style style;

    @ManyToMany(mappedBy = "bookmarkedMaterials")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Material() {
    }

    public Material(Long id, String title, FileType fileType, String fileName, Set<String> instruments, String category, Style style) {
        this.id = id;
        this.title = title;
        this.fileType = fileType;
        this.fileName = fileName;
        this.instruments = instruments;
        this.category = category;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<String> getInstruments() {return instruments;}

    public void setInstruments(Set<String> instruments) {this.instruments = instruments;}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Set<User> getUsers() {return users;}

    public void setUsers(Set<User> users) {this.users = users;}
}
