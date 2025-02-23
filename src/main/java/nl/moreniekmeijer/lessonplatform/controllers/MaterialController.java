package nl.moreniekmeijer.lessonplatform.controllers;

import jakarta.validation.Valid;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.models.Material;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    // Service injection here

    @PostMapping
    public ResponseEntity<Material> addTelevision(@Valid @RequestBody MaterialInputDto materialInputDto) {
        // Service method here
        return ResponseEntity.created(null).body(MaterialMapper.toEntity(materialInputDto));
    }
}