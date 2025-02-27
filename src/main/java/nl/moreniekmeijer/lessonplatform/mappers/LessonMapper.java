package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Style;

import java.util.stream.Collectors;

public class LessonMapper {
    public static Lesson toEntity(LessonInputDto dto) {
        Lesson lesson = new Lesson();
        lesson.setScheduledDate(dto.getScheduledDate());
        return lesson;
    }

    public static LessonResponseDto toResponseDto(Lesson lesson) {
        return new LessonResponseDto(
                lesson.getId(),
                lesson.getScheduledDate(),
                lesson.getStyles() != null ? lesson.getStyles().stream().map(Style::getId).toList() : null
        );
    }
}
