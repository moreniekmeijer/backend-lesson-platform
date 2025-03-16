package nl.moreniekmeijer.lessonplatform.dtos;

import nl.moreniekmeijer.lessonplatform.models.Style;

import java.util.List;
import java.time.LocalDate;

public class LessonResponseDto {
    private Long id;
    private LocalDate scheduledDate;
    private String notes;
    private List<Style> styles;

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