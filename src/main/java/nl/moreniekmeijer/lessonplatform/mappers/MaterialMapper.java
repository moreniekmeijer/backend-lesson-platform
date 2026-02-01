package nl.moreniekmeijer.lessonplatform.mappers;

import nl.moreniekmeijer.lessonplatform.dtos.MaterialInputDto;
import nl.moreniekmeijer.lessonplatform.dtos.MaterialResponseDto;
import nl.moreniekmeijer.lessonplatform.models.FileType;
import nl.moreniekmeijer.lessonplatform.models.Material;
import nl.moreniekmeijer.lessonplatform.models.Style;
import nl.moreniekmeijer.lessonplatform.utils.FileServiceHolder;

public class MaterialMapper {

//    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
//    private static final String BASE_URL = System.getenv("BASE_URL") != null
//            ? System.getenv("BASE_URL")
//            : DEFAULT_BASE_URL;

//    TODO: get BASE_URL out of Google Variables

    public static Material toEntity(MaterialInputDto dto, Style style) {
        Material material = new Material();
        material.setTitle(dto.getTitle());
        material.setInstruments(dto.getInstruments());
        material.setCategory(dto.getCategory());
        material.setStyle(style);
        return material;
    }

    public static MaterialResponseDto toResponseDto(Material material) {
        MaterialResponseDto responseDto = new MaterialResponseDto();
        responseDto.setId(material.getId());
        responseDto.setTitle(material.getTitle());
        responseDto.setFileType(material.getFileType());
        responseDto.setInstruments(material.getInstruments());
        responseDto.setCategory(material.getCategory());
        responseDto.setStyleName(material.getStyle() != null ? material.getStyle().getName() : null);
        responseDto.setOrigin(material.getStyle() != null ? material.getStyle().getOrigin() : null);

        if (material.getFileType() == FileType.LINK) {
            responseDto.setFileLink(material.getFileName());
            responseDto.setDownloadLink(null);
        } else if (material.getFileName() != null) {
            String viewUrl = FileServiceHolder.generateSignedUrl(
                    material.getFileName(),
                    false,
                    material.getTitle()
            );
            responseDto.setFileLink(viewUrl);

            String downloadUrl = FileServiceHolder.generateSignedUrl(
                    material.getFileName(),
                    true,
                    material.getTitle()
            );
            responseDto.setDownloadLink(downloadUrl);
        } else {
            responseDto.setFileLink(null);
            responseDto.setDownloadLink(null);
        }

        return responseDto;
    }
}
