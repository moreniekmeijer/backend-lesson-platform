package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.services.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<LessonResponseDto> createLesson(@Valid @RequestBody LessonInputDto lessonInputDto) {
        LessonResponseDto createdLesson = lessonService.createLesson(lessonInputDto);
        return ResponseEntity.ok(createdLesson);
    }

    @GetMapping("/next")
    public ResponseEntity<LessonResponseDto> getNextLesson() {
        LessonResponseDto nextLesson = lessonService.getNextLesson();
        return ResponseEntity.ok(nextLesson);
    }
}
