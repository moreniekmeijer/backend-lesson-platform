package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class StyleInputDto {

    @NotBlank(message = "Name is required")
    @Column(unique=true)
    private String name;

    @NotNull(message = "Origin is required")
    private String origin;

    private String description;

    private List<Long> lessonIds;

    private List<Long> materialIds;

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
