package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;

public class MaterialMapper {

    public static Material toEntity(MaterialInputDto dto, Style style) {
        Material material = new Material();
        material.setTitle(dto.getTitle());
        material.setFileType(dto.getFileType());
        material.setFilePath(dto.getFilePath());
        material.setInstrument(dto.getInstrument());
        material.setStyle(style);
        return material;
    }

    public static MaterialResponseDto toResponseDto(Material material) {
        String fileLink = null;

        if (material.getFileType() != null && material.getFilePath() != null) {
            if (material.getFileType() == FileType.LINK) {
                fileLink = "http://localhost:8080/materials/" + material.getId() + "/link";
            } else {
                fileLink = "http://localhost:8080/materials/" + material.getId() + "/file";
            }
        }
        MaterialResponseDto responseDto = new MaterialResponseDto();
        responseDto.setId(material.getId());
        responseDto.setTitle(material.getTitle());
        responseDto.setFileType(material.getFileType());
        responseDto.setFilePath(material.getFilePath());
        responseDto.setInstrument(material.getInstrument());
        responseDto.setCategory(material.getCategory());
        responseDto.setStyleName(material.getStyle() != null ? material.getStyle().getName() : null);
        responseDto.setOrigin(material.getStyle() != null ? material.getStyle().getOrigin() : null);
        responseDto.setFileLink(fileLink);

        return responseDto;
    }
}
