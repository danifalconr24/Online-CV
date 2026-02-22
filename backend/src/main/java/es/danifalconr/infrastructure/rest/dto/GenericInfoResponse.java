package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.GenericInfo;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record GenericInfoResponse(
        @Schema(description = "Unique identifier") Long id,
        @Schema(description = "About me text") String aboutMe,
        @Schema(description = "Base64-encoded profile image") String profileImage
) {

    public static GenericInfoResponse fromDomain(GenericInfo model) {
        return new GenericInfoResponse(model.id(), model.aboutMe(), model.profileImage());
    }
}
