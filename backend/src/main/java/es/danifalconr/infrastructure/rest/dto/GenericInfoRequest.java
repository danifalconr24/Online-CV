package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.GenericInfo;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record GenericInfoRequest(
        @NotNull @Schema(description = "About me text") String aboutMe,
        @Schema(description = "Base64-encoded profile image") String profileImage
) {

    public GenericInfo toDomain() {
        return new GenericInfo(null, aboutMe, profileImage);
    }
}
