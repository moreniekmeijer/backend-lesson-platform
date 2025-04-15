package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Material;

import java.util.List;
import java.util.Objects;

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
                ? style.getLessons().stream()
                .map(Lesson::getId)
                .toList()
                : List.of());

        List<MaterialResponseDto> materialResponseDtos = style.getMaterials().stream()
                .map(MaterialMapper::toResponseDto)
                .toList();

        responseDto.setMaterials(materialResponseDtos);

        responseDto.setLinks(
                materialResponseDtos.stream()
                        .filter(dto -> dto.getFileType() == FileType.LINK)
                        .map(MaterialResponseDto::getFileLink)
                        .filter(Objects::nonNull)
                        .toList()
        );

        responseDto.setArrangementId(
                style.getMaterials().stream()
                        .filter(material -> "arrangement".equalsIgnoreCase(material.getCategory()) && "PDF".equalsIgnoreCase(material.getFileType().name()))
                        .map(Material::getId)
                        .findFirst()
                        .orElse(null)
        );

        return responseDto;
    }
}
