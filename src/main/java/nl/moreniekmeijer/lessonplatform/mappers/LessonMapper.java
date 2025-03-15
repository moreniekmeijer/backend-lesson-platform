package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Style;

import java.util.List;
import java.util.stream.Collectors;

public class LessonMapper {
    public static Lesson toEntity(LessonInputDto dto, List<Style> styles) {
        Lesson lesson = new Lesson();
        lesson.setScheduledDate(dto.getScheduledDate());
        lesson.setNotes(dto.getNotes());
        lesson.setStyles(styles);
        return lesson;
    }

    public static LessonResponseDto toResponseDto(Lesson lesson) {
        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(lesson.getId());
        dto.setScheduledDate(lesson.getScheduledDate());
        dto.setNotes(lesson.getNotes());
        if (lesson.getStyles() != null) {
            dto.setStyleIds(lesson.getStyles().stream().map(Style::getId).toList());
        }
        return dto;
    }
}
