package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.services.MaterialService;
import nl.moreniekmeijer.lessonplatform.utils.URIUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public ResponseEntity<MaterialResponseDto> addMaterial(@Valid @RequestBody MaterialInputDto materialInputDto) {
        MaterialResponseDto savedMaterial = materialService.addMaterial(materialInputDto);
        URI location = URIUtil.createResourceUri(savedMaterial.getId());
        return ResponseEntity.created(location).body(savedMaterial);
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponseDto>> getFilteredMaterials(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String instrument,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String styleName,
            @RequestParam(required = false) String origin
    ) {
        List<MaterialResponseDto> filteredMaterials = materialService.getFilteredMaterials(
                search, fileType, instrument, category, styleName, origin
        );
        return ResponseEntity.ok(filteredMaterials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponseDto> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}