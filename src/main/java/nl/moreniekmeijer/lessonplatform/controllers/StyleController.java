package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleResponseDto;
import nl.moreniekmeijer.lessonplatform.services.MaterialService;
import nl.moreniekmeijer.lessonplatform.services.StyleService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/styles")
public class StyleController {

    private final StyleService styleService;

    public StyleController(StyleService styleService, MaterialService materialService) {
        this.styleService = styleService;
    }

    @PostMapping
    public ResponseEntity<StyleResponseDto> addStyle(@Valid @RequestBody StyleInputDto styleInputDto) {
        StyleResponseDto savedStyle = styleService.addStyle(styleInputDto);
        URI location = URIUtil.createResourceUri(savedStyle.getId());
        return ResponseEntity.created(location).body(savedStyle);
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

    @PutMapping("/{styleId}/materials/{materialId}")
    public ResponseEntity<StyleResponseDto> assignMaterialToStyle(@Valid @PathVariable Long styleId, @PathVariable Long materialId) {
        StyleResponseDto updatedStyle = styleService.assignMaterialToStyle(styleId, materialId);
        return ResponseEntity.ok(updatedStyle);
    }
}
