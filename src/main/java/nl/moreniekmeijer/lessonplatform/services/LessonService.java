package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.LessonMapper;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.LessonRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StyleRepository styleRepository;

    public LessonService(LessonRepository lessonRepository, StyleRepository styleRepository) {
        this.lessonRepository = lessonRepository;
        this.styleRepository = styleRepository;
    }

    public LessonResponseDto createLesson(LessonInputDto lessonInputDto) {
        // Lijst van stijlen ophalen op basis van styleIds
        Set<Style> styles = new HashSet<>();

        if (lessonInputDto.getStyleIds() != null && !lessonInputDto.getStyleIds().isEmpty()) {
            styles.addAll(styleRepository.findAllById(lessonInputDto.getStyleIds()));
        }

        // Lijst van stijlen ophalen op basis van styleNames
        if (lessonInputDto.getStyleNames() != null && !lessonInputDto.getStyleNames().isEmpty()) {
            List<Style> stylesFromNames = lessonInputDto.getStyleNames().stream()
                    .map(name -> styleRepository.findByName(name)
                            .orElseThrow(() -> new EntityNotFoundException("Style not found with name: " + name)))
                    .toList();
            styles.addAll(stylesFromNames);
        }

        Lesson lesson = LessonMapper.toEntity(lessonInputDto, styles);
        Lesson savedLesson = lessonRepository.save(lesson);
        return LessonMapper.toResponseDto(savedLesson);
    }


    public LessonResponseDto getNextLesson() {
        LocalDateTime now = LocalDateTime.now();
        Lesson nextLesson = lessonRepository
                .findFirstByScheduledDateTimeAfterOrderByScheduledDateTimeAsc(now)
                .orElseThrow(() -> new EntityNotFoundException("No upcoming lessons found"));
        return LessonMapper.toResponseDto(nextLesson);
    }
}
