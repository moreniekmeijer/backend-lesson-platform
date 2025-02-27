package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import nl.moreniekmeijer.lessonplatform.models.LessonStatus;

import java.time.LocalDate;
import java.util.List;

public class LessonInputDto {
    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    @NotNull(message = "Status is required")
    private LessonStatus status;

    @NotEmpty(message = "A lesson must have at least one style")
    private List<Long> styleIds;

    public LessonInputDto() {
    }

    public LessonInputDto(LocalDate scheduledDate, LessonStatus status, List<Long> styleIds) {
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.styleIds = styleIds;
    }

    public @NotNull(message = "Scheduled date is required") LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(@NotNull(message = "Scheduled date is required") LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public @NotNull(message = "Status is required") LessonStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Status is required") LessonStatus status) {
        this.status = status;
    }

    public List<Long> getStyleIds() {
        return styleIds;
    }

    public void setStyleIds(List<Long> styleIds) {
        this.styleIds = styleIds;
    }
}
