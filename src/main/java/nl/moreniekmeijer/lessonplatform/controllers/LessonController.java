package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.LessonInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.LessonResponseDto;
import nl.moreniekmeijer.lessonplatform.services.LessonService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
        URI location = URIUtil.createResourceUri(createdLesson.getId());
        return ResponseEntity.created(location).body(createdLesson);
    }

    @GetMapping
    public ResponseEntity<List<LessonResponseDto>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @GetMapping("/next")
    public ResponseEntity<LessonResponseDto> getNextLesson() {
        return ResponseEntity.ok(lessonService.getNextLesson());
    }
}
