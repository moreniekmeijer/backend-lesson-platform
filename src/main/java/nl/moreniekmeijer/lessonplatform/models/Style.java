package nl.moreniekmeijer.lessonplatform.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "styles")
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String origin;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "lessons_styles",
            joinColumns = @JoinColumn(name = "styles_id"),
            inverseJoinColumns = @JoinColumn(name = "lessons_id")
    )
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "style", cascade = CascadeType.ALL)
    private List<Material> materials;

    public Style() {
    }

    public Style(Long id, String name, String origin, String description, List<Lesson> lessons, List<Material> materials) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.description = description;
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