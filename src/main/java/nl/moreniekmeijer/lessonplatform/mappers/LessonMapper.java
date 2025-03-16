package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Style;

import java.util.List;
import java.util.Set;

public class LessonMapper {
    public static Lesson toEntity(LessonInputDto dto, Set<Style> styles) {
        Lesson lesson = new Lesson();
        lesson.setScheduledDateTime(dto.getScheduledDateTime());
        lesson.setNotes(dto.getNotes());
        lesson.setStyles(styles);
        return lesson;
    }

    public static LessonResponseDto toResponseDto(Lesson lesson) {
        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(lesson.getId());
        dto.setScheduledDateTime(lesson.getScheduledDateTime());
        dto.setNotes(lesson.getNotes());
        if (lesson.getStyles() != null && !lesson.getStyles().isEmpty()) {
            dto.setStyles(lesson.getStyles());
        }
        return dto;
    }

}
