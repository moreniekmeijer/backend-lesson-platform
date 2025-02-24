package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;

public class MaterialMapper {

    public static Material toEntity(MaterialInputDto dto, Style style) {
        Material material = new Material();
        material.setTitle(dto.getTitle());
        material.setFileType(dto.getFileType());
        material.setFilePath(dto.getFilePath());
        material.setLink(dto.getLink());
        material.setInstrument(dto.getInstrument());
        material.setStyle(style);
        return material;
    }

    public static MaterialResponseDto toResponseDto(Material material) {
        return new MaterialResponseDto(
                material.getId(),
                material.getTitle(),
                material.getFileType().name(),
                material.getFilePath(),
                material.getLink(),
                material.getInstrument(),
                material.getStyle() != null ? material.getStyle().getName() : null
        );
    }
}
