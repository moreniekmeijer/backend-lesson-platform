package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.StyleMapper;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.MaterialRepository;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleService {

    private final StyleRepository styleRepository;
    private final MaterialRepository materialRepository;

    public StyleService(StyleRepository styleRepository, MaterialRepository materialRepository) {
        this.styleRepository = styleRepository;
        this.materialRepository = materialRepository;
    }

    public StyleResponseDto addStyle(StyleInputDto styleInputDto) {
        Style style = StyleMapper.toEntity(styleInputDto);
        try {
            Style savedStyle = styleRepository.save(style);
            return StyleMapper.toResponseDto(savedStyle);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("The name must be unique. This name already exists.");
        }
    }

    // TODO - Onderstaande methode uitbouwen met bepaalde RequestParam's?
    public List<StyleResponseDto> getAllStyles() {
        List<Style> styles = styleRepository.findAll();
        return styles.stream()
                .map(StyleMapper::toResponseDto)
                .toList();
    }

    public StyleResponseDto getStyleById(Long id) {
        Style foundStyle = styleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Style not found with id: " + id));
        return StyleMapper.toResponseDto(foundStyle);
    }

    public void deleteStyle(Long id) {
        Style foundStyle = styleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Style not found with id: " + id));
        styleRepository.delete(foundStyle);
    }

    @Transactional
    public StyleResponseDto assignMaterialToStyle(Long styleId, Long materialId) {
        Style foundStyle = styleRepository.findById(styleId).orElseThrow(() -> new EntityNotFoundException("Style not found with id: " + styleId));
        Material foundMaterial = materialRepository.findById(materialId).orElseThrow(() -> new EntityNotFoundException("Material not found with id: " + materialId));

        // Onderstaande code verwijdert de gekoppelde stijl van het gevonden materiaal.
        // Misschien een soort waarschuwing naar de Frontend?
        if (foundMaterial.getStyle() != null) {
            foundMaterial.getStyle().getMaterials().removeIf(material -> material.getId().equals(foundMaterial.getId()));
        }

        foundMaterial.setStyle(foundStyle);
        foundStyle.getMaterials().add(foundMaterial);

        return StyleMapper.toResponseDto(foundStyle);
    }
}
