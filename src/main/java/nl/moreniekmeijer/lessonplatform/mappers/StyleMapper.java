package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Material;

import java.util.List;

public class StyleMapper {
    public static Style toEntity(StyleInputDto dto) {
        Style style = new Style();
        style.setName(dto.getName());
        style.setOrigin(dto.getOrigin());
        style.setDescription(dto.getDescription());
        return style;
    }

    public static StyleResponseDto toResponseDto(Style style) {
        StyleResponseDto responseDto = new StyleResponseDto();
        responseDto.setId(style.getId());
        responseDto.setName(style.getName());
        responseDto.setOrigin(style.getOrigin());
        responseDto.setDescription(style.getDescription());
        responseDto.setLessonIds(style.getLessons() != null
                ? style.getLessons().stream().map(Lesson::getId).toList()
                : null);
        responseDto.setMaterialIds(style.getMaterials() != null
                ? style.getMaterials().stream().map(Material::getId).toList()
                : null);
        responseDto.setLinks(style.getMaterials() != null
                ? style.getMaterials().stream()
                .filter(material -> material.getFileType() == FileType.LINK)
                .map(Material::getFilePath)
                .toList()
                : List.of());

        return responseDto;
    }
}
