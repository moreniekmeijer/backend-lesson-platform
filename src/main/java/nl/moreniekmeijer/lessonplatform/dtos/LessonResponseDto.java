package nl.moreniekmeijer.lessonplatform.dtos;

import java.time.LocalDateTime;
import java.util.Set;

public class LessonResponseDto {
    private Long id;
    private LocalDateTime scheduledDateTime;
    private String notes;
    private Set<String> allowedRoles;
    private Set<Long> styleIds;

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

    public void setStyleIds(Set<Long> styleIds) {
        this.styleIds = styleIds;
    }

    public Set<Long> getStyleIds() {
        return styleIds;
    }
}