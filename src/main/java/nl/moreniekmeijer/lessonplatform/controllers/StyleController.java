package nl.moreniekmeijer.lessonplatform.controllers;

import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleResponseDto;
import nl.moreniekmeijer.lessonplatform.services.MaterialService;
import nl.moreniekmeijer.lessonplatform.services.StyleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/styles")
public class StyleController {

    private final StyleService styleService;
    private final MaterialService materialService;

    public StyleController(StyleService styleService, MaterialService materialService) {
        this.styleService = styleService;
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<StyleResponseDto> addStyle(@RequestBody StyleInputDto styleInputDto) {
        StyleResponseDto savedStyle = styleService.addStyle(styleInputDto);
        return ResponseEntity.created(null).body(savedStyle);
    }

    @GetMapping
    public ResponseEntity<List<StyleResponseDto>> getAllStyles() {
        return ResponseEntity.ok(styleService.getAllStyles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StyleResponseDto> getStyleById(@PathVariable Long id) {
        return ResponseEntity.ok(styleService.getStyleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStyle(@PathVariable Long id) {
        styleService.deleteStyle(id);
        return ResponseEntity.noContent().build();
    }
}
