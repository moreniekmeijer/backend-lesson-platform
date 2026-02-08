package nl.moreniekmeijer.lessonplatform.dtos;

import jakarta.validation.constraints.NotNull;

public class ArrangementInputDto {

    @NotNull
    private String arrangement;

    public String getArrangement() {
        return arrangement;
    }

    public void setArrangement(String arrangement) {
        this.arrangement = arrangement;
    }
}

