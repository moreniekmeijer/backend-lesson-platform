package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.LessonMapper;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.LessonRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StyleRepository styleRepository;

    public LessonService(LessonRepository lessonRepository, StyleRepository styleRepository) {
        this.lessonRepository = lessonRepository;
        this.styleRepository = styleRepository;
    }

    public LessonResponseDto createLesson(LessonInputDto lessonInputDto, List<String> styleNames) {
        List<Style> styles = styleNames.stream()
                .map(name -> styleRepository.findByName(name)
                        .orElseThrow(() -> new EntityNotFoundException("Style not found: " + name)))
                .toList();

        Lesson createdLesson = LessonMapper.toEntity(lessonInputDto, styles);
        Lesson savedLesson = lessonRepository.save(createdLesson);
        return LessonMapper.toResponseDto(savedLesson);
    }
}
