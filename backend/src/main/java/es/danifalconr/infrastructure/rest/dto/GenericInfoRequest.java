package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.GenericInfo;
import jakarta.validation.constraints.NotNull;

public record GenericInfoRequest(@NotNull String aboutMe, String profileImage) {

    public GenericInfo toDomain() {
        return new GenericInfo(null, aboutMe, profileImage);
    }
}
