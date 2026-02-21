package es.danifalconr.infrastructure.rest.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
