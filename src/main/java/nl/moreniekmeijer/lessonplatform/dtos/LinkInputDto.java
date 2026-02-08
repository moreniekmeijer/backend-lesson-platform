package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LinkInputDto {

    @NotNull(message = "Link is required")
    @Pattern(
            regexp = "^(https?://)?([\\w-]+(\\.[\\w-]+)+)(/.*)?$",
            message = "Invalid link format"
    )
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
