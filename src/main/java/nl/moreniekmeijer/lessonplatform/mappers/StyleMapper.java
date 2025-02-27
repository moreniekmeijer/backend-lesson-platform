package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.StyleInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.StyleResponseDto;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.models.Lesson;
import nl.moreniekmeijer.lessonplatform.models.Material;

public class StyleMapper {
    public static Style toEntity(StyleInputDto dto) {
        Style style = new Style();
        style.setName(dto.getName());
        style.setOrigin(dto.getOrigin());
        style.setDescription(dto.getDescription());
        return style;
    }

    public static StyleResponseDto toResponseDto(Style style) {
        return new StyleResponseDto(
                style.getId(),
                style.getName(),
                style.getOrigin(),
                style.getDescription(),
                style.getLessons() != null ? style.getLessons().stream().map(Lesson::getId).toList() : null,
                style.getMaterials() != null ? style.getMaterials().stream().map(Material::getId).toList() : null
        );
    }
}
