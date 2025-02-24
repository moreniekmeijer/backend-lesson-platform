package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final StyleRepository styleRepository;

    public MaterialService(MaterialRepository materialRepository, StyleRepository styleRepository) {
        this.materialRepository = materialRepository;
        this.styleRepository = styleRepository;
    }

    public MaterialResponseDto addMaterial(MaterialInputDto materialInputDto) {
        // Style ophalen (als die bestaat)
        Style style = null;
        if (materialInputDto.getStyleId() != null) {
            style = styleRepository.findById(materialInputDto.getStyleId())
                    .orElseThrow(() -> new EntityNotFoundException("Style not found with id: " + materialInputDto.getStyleId()));
        }

        // DTO omzetten naar entity
        Material material = MaterialMapper.toEntity(materialInputDto, style);

        // Opslaan in de database
        Material savedMaterial = materialRepository.save(material);

        // Omzetten naar Response DTO
        return MaterialMapper.toResponseDto(savedMaterial);
    }

    public List<MaterialResponseDto> getAllMaterials() {
        List<Material> materials = materialRepository.findAll();
        return materials.stream()
                .map(MaterialMapper::toResponseDto)
                .toList();
    }
}
