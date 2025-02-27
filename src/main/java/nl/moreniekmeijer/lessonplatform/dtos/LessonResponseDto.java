package nl.moreniekmeijer.lessonplatform.dtos;

import java.util.List;
import java.time.LocalDate;

public class LessonResponseDto {
    private Long id;
    private LocalDate scheduledDate;
    private List<Long> styleIds;

    public LessonResponseDto() {
    }

    public LessonResponseDto(Long id, LocalDate scheduledDate, List<Long> styleIds) {
        this.id = id;
        this.scheduledDate = scheduledDate;
        this.styleIds = styleIds;
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

    public List<Long> getStyleIds() {
        return styleIds;
    }

    public void setStyleIds(List<Long> styleIds) {
        this.styleIds = styleIds;
    }
}