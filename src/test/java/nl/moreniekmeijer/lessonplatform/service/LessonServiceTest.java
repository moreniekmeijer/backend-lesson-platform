//package nl.moreniekmeijer.lessonplatform.service;
//
//import jakarta.persistence.EntityNotFoundException;
//import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
//import nl.moreniekmeijer.lessonplatform.models.Lesson;
//import nl.moreniekmeijer.lessonplatform.models.Style;
//import nl.moreniekmeijer.lessonplatform.repositories.LessonRepository;
//import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class LessonServiceTest {
//
//    @Mock
//    LessonRepository lessonRepository;
//
//    @Mock
//    StyleRepository styleRepository;
//
//    @InjectMocks
//    LessonService lessonService;
//
//    @Test
//    void createLesson_shouldThrowIfNoStylesProvided() {
//        LessonInputDto input = new LessonInputDto();
//        input.setStyleIds(null);
//        input.setStyleNames(null);
//
//        var exception = assertThrows(IllegalArgumentException.class, () -> lessonService.createLesson(input));
//        assertEquals("At least one of styleIds ór styleNames must be provided", exception.getMessage());
//    }
//
//    @Test
//    void createLesson_shouldMapFromIdsAndSave() {
//        LessonInputDto input = new LessonInputDto();
//        input.setStyleIds(List.of(1L));
//        input.setStyleNames(null);
//
//        Style style = new Style();
//        style.setId(1L);
//        when(styleRepository.findAllById(List.of(1L))).thenReturn(List.of(style));
//        when(lessonRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
//
//        lessonService.createLesson(input);
//
//        verify(lessonRepository).save(any());
//    }
//
//    @Test
//    void createLesson_shouldMapFromNamesAndSave() {
//        LessonInputDto input = new LessonInputDto();
//        input.setStyleIds(null);
//        input.setStyleNames(List.of("Baiao"));
//
//        Style style = new Style();
//        style.setName("Baiao");
//        when(styleRepository.findByName("Baiao")).thenReturn(Optional.of(style));
//        when(lessonRepository.save(any())).thenAnswer(invalid -> invalid.getArgument(0));
//
//        lessonService.createLesson(input);
//
//        verify(lessonRepository).save(any());
//    }
//
//    @Test
//    void createLesson_shouldThrowIfStyleNameNotFound() {
//        LessonInputDto input = new LessonInputDto();
//        input.setStyleNames(List.of("Unknown"));
//
//        when(styleRepository.findByName("Unknown")).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> lessonService.createLesson(input));
//    }
//
//    @Test
//    void getAllLessons_shouldReturnAllLessons() {
//        Lesson lesson = new Lesson();
//        lesson.setStyles(new HashSet<>());
//        when(lessonRepository.findAllByOrderByScheduledDateTimeAsc()).thenReturn(List.of(lesson));
//
//        var result = lessonService.getAllLessons();
//
//        assertEquals(1, result.size());
//    }
//
////    @Test
////    void getNextLesson_shouldReturnNextLesson() {
////        Lesson lesson = new Lesson();
////        lesson.setStyles(new HashSet<>());
////        when(lessonRepository.findFirstByScheduledDateTimeAfterOrderByScheduledDateTimeAsc(any()))
////                .thenReturn(Optional.of(lesson));
////
////        var result = lessonService.getNextLesson();
////
////        assertNotNull(result);
////    }
////
////    @Test
////    void getNextLesson_shouldThrowIfNoneFound() {
////        when(lessonRepository.findFirstByScheduledDateTimeAfterOrderByScheduledDateTimeAsc(any()))
////                .thenReturn(Optional.empty());
////
////        assertThrows(EntityNotFoundException.class, () -> lessonService.getNextLesson());
////    }
//
//    @Test
//    void deleteLesson_shouldClearStylesAndDelete() {
//        Lesson lesson = new Lesson();
//        lesson.setStyles(new HashSet<>(Set.of(new Style())));
//
//        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
//
//        lessonService.deleteLesson(1L);
//
//        assertTrue(lesson.getStyles().isEmpty());
//        verify(lessonRepository).save(lesson);
//        verify(lessonRepository).delete(lesson);
//    }
//}
