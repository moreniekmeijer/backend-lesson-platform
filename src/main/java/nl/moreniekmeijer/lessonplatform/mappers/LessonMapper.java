package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Style;

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
        LessonResponseDto responseDto = new LessonResponseDto();
        responseDto.setId(lesson.getId());
        responseDto.setScheduledDateTime(lesson.getScheduledDateTime());
        responseDto.setNotes(lesson.getNotes());
        responseDto.setStyles(lesson.getStyles());
        return responseDto;
    }

}
