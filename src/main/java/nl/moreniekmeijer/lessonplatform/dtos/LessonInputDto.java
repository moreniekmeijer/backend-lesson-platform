package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class LessonInputDto {
    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;
    private String notes;

//    @NotNull(message = "Status is required")
//    private LessonStatus status;

    @NotEmpty(message = "A lesson must have at least one style")
    private List<Long> styleIds;

    public @NotNull(message = "Scheduled date is required") LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(@NotNull(message = "Scheduled date is required") LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
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
}
