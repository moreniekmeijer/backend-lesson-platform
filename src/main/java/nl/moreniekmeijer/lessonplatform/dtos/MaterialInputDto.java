package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class MaterialInputDto {

    @NotBlank(message = "Title is required")
    private String title;

    private Set<String> instruments;

    private String category;

    @NotNull(message = "styleId is required")
    private Long styleId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<String> instruments) {
        this.instruments = instruments;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }
}
