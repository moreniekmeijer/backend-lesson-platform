package nl.moreniekmeijer.lessonplatform.dtos;

import java.util.List;

public class StyleResponseDto {
    private Long id;
    private String name;
    private String origin;
    private String description;
    private List<Long> lessonIds;
    private List<Long> materialIds;
    private List<String> links;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
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

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }
}
