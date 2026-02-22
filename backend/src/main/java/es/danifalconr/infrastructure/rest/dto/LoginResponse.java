package es.danifalconr.infrastructure.rest.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "JWT access token") String accessToken,
        @Schema(description = "Token type (Bearer)") String tokenType,
        @Schema(description = "Token expiration time in seconds") long expiresIn
) {
}
