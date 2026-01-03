package nl.moreniekmeijer.lessonplatform.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime scheduledDateTime;
    private String notes;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "lesson_roles",
            joinColumns = @JoinColumn(name = "lesson_id")
    )
    @Column(name = "role")
    private Set<String> allowedRoles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "lessons_styles",
            joinColumns = @JoinColumn(name = "lessons_id"),
            inverseJoinColumns = @JoinColumn(name = "styles_id")
    )
    private Set<Style> styles;

    public Lesson() {
    }

    public Lesson(Long id, LocalDateTime scheduledDateTime, String notes, Set<String> allowedRoles, Set<Style> styles) {
        this.id = id;
        this.scheduledDateTime = scheduledDateTime;
        this.notes = notes;
        this.allowedRoles = allowedRoles;
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

    public Set<String> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(Set<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public Set<Style> getStyles() {
        return styles;
    }

    public void setStyles(Set<Style> styles) {
        this.styles = styles;
    }
}
