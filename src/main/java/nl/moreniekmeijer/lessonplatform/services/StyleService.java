package nl.moreniekmeijer.lessonplatform.services;

import jakarta.persistence.EntityNotFoundException;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleResponseDto;
import nl.moreniekmeijer.lessonplatform.mappers.StyleMapper;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.repositories.StyleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleService {

    private final StyleRepository styleRepository;

    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public StyleResponseDto addStyle(StyleInputDto styleInputDto) {
        Style style = StyleMapper.toEntity(styleInputDto);
        Style savedStyle = styleRepository.save(style);
        return StyleMapper.toResponseDto(savedStyle);
    }

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
}
