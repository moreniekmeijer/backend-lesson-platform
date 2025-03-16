package nl.moreniekmeijer.lessonplatform.dtos;

import nl.moreniekmeijer.lessonplatform.models.Style;

import java.time.LocalDateTime;
import java.util.Set;

public class LessonResponseDto {
    private Long id;
    private LocalDateTime scheduledDateTime;
    private String notes;
    private Set<Style> styles;

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

    public Set<Style> getStyles() {
        return styles;
    }

    public void setStyles(Set<Style> styles) {
        this.styles = styles;
    }
}