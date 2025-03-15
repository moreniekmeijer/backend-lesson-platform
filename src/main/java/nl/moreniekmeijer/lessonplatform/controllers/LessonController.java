package nl.moreniekmeijer.lessonplatform.controllers;

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

    @PostMapping("/lessons/{styleIds}")
    public ResponseEntity<LessonResponseDto> createLesson(
            @RequestBody LessonInputDto dto,
            @PathVariable List<String> styleNames) {
        LessonResponseDto createdLesson = lessonService.createLesson(dto, styleNames);
        return ResponseEntity.ok(createdLesson);
    }

    @GetMapping
    public ResponseEntity<LessonResponseDto> getNextLesson() {

    }
}
