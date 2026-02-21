package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.GenericInfo;
import jakarta.validation.constraints.NotNull;

public record GenericInfoRequest(
        @NotNull String aboutMe
) {

    public GenericInfo toDomain() {
        GenericInfo model = new GenericInfo();
        model.setAboutMe(aboutMe);
        return model;
    }
}
