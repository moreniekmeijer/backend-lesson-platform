package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;

public class MaterialMapper {

    public static Material toEntity(MaterialInputDto dto, Style style) {
        Material material = new Material();
        material.setTitle(dto.getTitle());
        material.setInstrument(dto.getInstrument());
        material.setCategory(dto.getCategory());
        material.setStyle(style);
        return material;
    }

    public static MaterialResponseDto toResponseDto(Material material) {
        MaterialResponseDto responseDto = new MaterialResponseDto();
        responseDto.setId(material.getId());
        responseDto.setTitle(material.getTitle());
        responseDto.setFileType(material.getFileType());
        responseDto.setInstrument(material.getInstrument());
        responseDto.setCategory(material.getCategory());
        responseDto.setStyleName(material.getStyle() != null ? material.getStyle().getName() : null);
        responseDto.setOrigin(material.getStyle() != null ? material.getStyle().getOrigin() : null);

        responseDto.setFileLink(material.getFileName());

        return responseDto;
    }
}
