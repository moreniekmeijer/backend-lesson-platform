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

        Material material = MaterialMapper.toEntity(materialInputDto, style);
        Material savedMaterial = materialRepository.save(material);
        return MaterialMapper.toResponseDto(savedMaterial);
    }

    public List<MaterialResponseDto> getAllMaterials() {
        List<Material> foundMaterials = materialRepository.findAll();
        return foundMaterials.stream()
                .map(MaterialMapper::toResponseDto)
                .toList();
    }

    public MaterialResponseDto getMaterialById(Long id) {
        Material foundMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));
        return MaterialMapper.toResponseDto(foundMaterial);
    }

    public void deleteMaterial(Long id) {
        Material foundMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + id));
        materialRepository.delete(foundMaterial);
    }
}
