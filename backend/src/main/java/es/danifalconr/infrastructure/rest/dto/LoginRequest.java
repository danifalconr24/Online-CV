package es.danifalconr.infrastructure.rest.dto;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record LoginRequest(
        @NotNull @Schema(description = "Admin username") String username,
        @NotNull @Schema(description = "Admin password") String password
) {
}
