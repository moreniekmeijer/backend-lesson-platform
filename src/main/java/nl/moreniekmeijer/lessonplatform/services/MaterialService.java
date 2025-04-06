package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.MaterialMapper;
import nl.moreniekmeijer.lessonplatform.models.FileType;
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

    public List<MaterialResponseDto> getFilteredMaterials(
            String search, String fileType, String instrument,
            String category, String styleName, String origin
    ) {
        List<Material> foundMaterials = materialRepository.findAll();

        if (search != null && !search.isEmpty()) {
            foundMaterials = foundMaterials.stream()
                    .filter(m -> m.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        if (fileType != null && !fileType.isEmpty()) {
            try {
                FileType fileTypeEnum = FileType.valueOf(fileType.toUpperCase()); // Zet om naar ENUM
                foundMaterials = foundMaterials.stream()
                        .filter(m -> m.getFileType() == fileTypeEnum) // Vergelijk ENUM direct
                        .toList();
            } catch (IllegalArgumentException e) {
                System.out.println("Ongeldig bestandstype: " + fileType);
            }
        }

        if (instrument != null && !instrument.isEmpty()) {
            foundMaterials = foundMaterials.stream().filter(m -> m.getInstrument().equals(instrument)).toList();
        }

        if (category != null && !category.isEmpty()) {
            foundMaterials = foundMaterials.stream().filter(m -> m.getCategory().equals(category)).toList();
        }

        if (styleName != null && !styleName.isEmpty()) {
            foundMaterials = foundMaterials.stream()
                    .filter(m -> m.getStyle() != null && m.getStyle().getName().equals(styleName))
                    .toList();
        }

        if (origin != null && !origin.isEmpty()) {
            foundMaterials = foundMaterials.stream()
                    .filter(m -> m.getStyle() != null && m.getStyle().getOrigin().equals(origin))
                    .toList();
        }

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

    public MaterialResponseDto assignToMaterial(String fileNameOrLink, Long materialId, FileType fileType) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + materialId));

        // Als de fileType LINK is, behandel het dan als een link
        if (fileType == FileType.LINK) {
            material.setFilePath(fileNameOrLink); // Voor een link, stel het filePath in als de link
        } else {
            material.setFilePath(fileNameOrLink); // Voor bestanden, stel het filePath in als de bestandsnaam
        }

        material.setFileType(fileType);  // Stel het fileType in (VIDEO, AUDIO, PDF, LINK)

        // Update het Material in de database
        Material updatedMaterial = materialRepository.save(material);

        // Retourneer het resultaat
        return MaterialMapper.toResponseDto(updatedMaterial);
    }


}
