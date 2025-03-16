package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class LessonInputDto {
    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDateTime;
    private String notes;

//    @NotNull(message = "Status is required")
//    private LessonStatus status;

    @NotEmpty(message = "A lesson must have at least one style")
    private List<Long> styleIds;
    @NotEmpty(message = "A lesson must have at least one style")
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
