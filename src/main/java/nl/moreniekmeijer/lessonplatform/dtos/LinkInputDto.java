package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotNull;

public class LinkInputDto {

    @NotNull(message = "Link is required")
    private String link;

    public String getLink() {
        return link;
    }

    public void setUrl(String link) {
        this.link = link;
    }
}
