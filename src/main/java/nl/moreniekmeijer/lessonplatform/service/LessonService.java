package nl.moreniekmeijer.lessonplatform.service;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.LessonMapper;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.LessonRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StyleRepository styleRepository;

    public LessonService(LessonRepository lessonRepository, StyleRepository styleRepository) {
        this.lessonRepository = lessonRepository;
        this.styleRepository = styleRepository;
    }

    public LessonResponseDto createLesson(LessonInputDto lessonInputDto) {
        if ((lessonInputDto.getStyleIds() == null || lessonInputDto.getStyleIds().isEmpty()) &&
                (lessonInputDto.getStyleNames() == null || lessonInputDto.getStyleNames().isEmpty())) {
            throw new IllegalArgumentException("At least one of styleIds ór styleNames must be provided");
        }

        Set<Style> styles = new HashSet<>();

        if (lessonInputDto.getStyleIds() != null && !lessonInputDto.getStyleIds().isEmpty()) {
            styles.addAll(styleRepository.findAllById(lessonInputDto.getStyleIds()));
        }

        if (lessonInputDto.getStyleNames() != null && !lessonInputDto.getStyleNames().isEmpty()) {
            List<Style> stylesFromNames = lessonInputDto.getStyleNames().stream()
                    .map(name -> styleRepository.findByName(name)
                            .orElseThrow(() -> new EntityNotFoundException("Style not found with name: " + name)))
                    .toList();
            styles.addAll(stylesFromNames);
        }

        Lesson createdLesson = LessonMapper.toEntity(lessonInputDto, styles);

        if (createdLesson.getAllowedRoles().isEmpty()) {
            throw new IllegalArgumentException("Een les moet minstens één toegestane rol hebben (bijv. ROLE_GROUP_1 of ROLE_GROUP_2).");
        }

        Lesson savedLesson = lessonRepository.save(createdLesson);
        return LessonMapper.toResponseDto(savedLesson);
    }

    @Transactional(readOnly = true)
    public List<LessonResponseDto> getAllLessons() {
        Set<String> userRoles = getCurrentUserRoles();

        boolean isAdmin = userRoles.contains("ROLE_ADMIN");

        List<Lesson> lessons = lessonRepository.findAllByOrderByScheduledDateTimeAsc();

        return lessons.stream()
                .filter(lesson -> isAdmin || lesson.getAllowedRoles().stream().anyMatch(userRoles::contains))
                .map(LessonMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LessonResponseDto> getNextLessons() {
        LocalDateTime now = LocalDateTime.now();
        Set<String> userRoles = getCurrentUserRoles();

        // Admin wordt behandeld alsof hij beide groepen heeft
        if (userRoles.contains("ROLE_ADMIN")) {
            userRoles = Set.of("ROLE_GROUP_1", "ROLE_GROUP_2");
        }

        // Filter alleen groepsrollen
        List<String> groupRoles = userRoles.stream()
                .filter(r -> r.startsWith("ROLE_GROUP_"))
                .toList();

        if (groupRoles.isEmpty()) {
            throw new EntityNotFoundException("Geen lessen beschikbaar voor jouw groep.");
        }

        // Vind per groep de eerstvolgende les en verzamel ze
        List<Lesson> lessons = groupRoles.stream()
                .flatMap(role -> lessonRepository.findNextLessonsForRole(role, now).stream().limit(1))
                .toList();

        if (lessons.isEmpty()) {
            throw new EntityNotFoundException("Geen lessen gevonden voor jouw groepen.");
        }

        return lessons.stream()
                .map(LessonMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public void deleteLesson(Long id) {
        Lesson foundLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found with id: " + id));

        foundLesson.getStyles().clear();
        foundLesson.getAllowedRoles().clear();

        lessonRepository.save(foundLesson);
        lessonRepository.delete(foundLesson);
    }

    private Set<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return Set.of();
        }
        return authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet());
    }
}
