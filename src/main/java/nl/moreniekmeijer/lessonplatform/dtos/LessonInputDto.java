package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class LessonInputDto {
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDateTime;
    private String notes;
    private Set<String> allowedRoles;
    private List<Long> styleIds;
    private List<String> styleNames;

    public @NotNull(message = "Scheduled date is required") LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(@NotNull(message = "Scheduled date is required") LocalDateTime scheduledDateTime) {
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

    public List<Long> getStyleIds() {
        return styleIds;
    }

    public void setStyleIds(List<Long> styleIds) {
        this.styleIds = styleIds;
    }

    public List<String> getStyleNames() {
        return styleNames;
    }

    public void setStyleNames(List<String> styleNames) {
        this.styleNames = styleNames;
    }
}
