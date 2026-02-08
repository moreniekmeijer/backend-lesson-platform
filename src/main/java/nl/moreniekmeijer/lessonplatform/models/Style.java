package nl.moreniekmeijer.lessonplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "styles")
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String origin;

    @Column(length = 1000)
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String arrangement;

    @ManyToMany(mappedBy = "styles")
    @JsonIgnore
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "style", cascade = CascadeType.ALL)
    private List<Material> materials = new ArrayList<>();

    public Style() {
    }

    public Style(Long id, String name, String origin, String description, String arrangement, List<Lesson> lessons, List<Material> materials) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.description = description;
        this.arrangement = arrangement;
        this.lessons = lessons;
        this.materials = materials;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getArrangement() {
        return arrangement;
    }

    public void setArrangement(String arrangement) {
        this.arrangement = arrangement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
}