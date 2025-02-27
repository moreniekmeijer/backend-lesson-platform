package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.moreniekmeijer.lessonplatform.models.FileType;

import java.util.List;

public class StyleInputDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Origin is required")
    private String origin;

    private String description;

    private List<Long> lessonIds;

    private List<Long> materialIds;

    public StyleInputDto() {
    }

    public StyleInputDto(String name, String origin, String description, List<Long> lessonIds, List<Long> materialIds) {
        this.name = name;
        this.origin = origin;
        this.description = description;
        this.lessonIds = lessonIds;
        this.materialIds = materialIds;
    }

    public @NotBlank(message = "Name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is required") String name) {
        this.name = name;
    }

    public @NotNull(message = "Origin is required") String getOrigin() {
        return origin;
    }

    public void setOrigin(@NotNull(message = "Origin is required") String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getLessonIds() {
        return lessonIds;
    }

    public void setLessonIds(List<Long> lessonIds) {
        this.lessonIds = lessonIds;
    }

    public List<Long> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<Long> materialIds) {
        this.materialIds = materialIds;
    }
}
