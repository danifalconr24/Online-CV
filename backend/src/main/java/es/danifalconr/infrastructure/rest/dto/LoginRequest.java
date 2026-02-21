package es.danifalconr.infrastructure.rest.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull String username,
        @NotNull String password
) {
}
