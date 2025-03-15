package nl.moreniekmeijer.lessonplatform.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate scheduledDate;
    private String notes;

//    @Enumerated(EnumType.STRING)
//    private LessonStatus status;

    @ManyToMany(mappedBy = "lessons")
    @NotEmpty(message = "A lesson must have at least one style")
    private List<Style> styles;


    public Lesson() {
    }

    public Lesson(Long id, LocalDate scheduledDate, String notes, List<Style> styles) {
        this.id = id;
        this.scheduledDate = scheduledDate;
        this.notes = notes;
        this.styles = styles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Style> getStyles() {
        return styles;
    }

    public void setStyles(List<Style> styles) {
        this.styles = styles;
    }
}
