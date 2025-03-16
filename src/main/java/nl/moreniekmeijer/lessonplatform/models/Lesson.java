package nl.moreniekmeijer.lessonplatform.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime scheduledDateTime;
    private String notes;

//    @Enumerated(EnumType.STRING)
//    private LessonStatus status;

    @ManyToMany(mappedBy = "lessons")
    @NotEmpty(message = "A lesson must have at least one style")
    private List<Style> styles;

    public Lesson() {
    }

    public Lesson(Long id, LocalDateTime scheduledDateTime, String notes, List<Style> styles) {
        this.id = id;
        this.scheduledDateTime = scheduledDateTime;
        this.notes = notes;
        this.styles = styles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
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
