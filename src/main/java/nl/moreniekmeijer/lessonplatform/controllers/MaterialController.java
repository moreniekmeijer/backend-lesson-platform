package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.services.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.created(null).body(savedMaterial);
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponseDto>> getAllMaterials() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }
}