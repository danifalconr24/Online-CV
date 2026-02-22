package es.danifalconr.domain.model;

public record AuthToken(String accessToken, String tokenType, long expiresIn) {
}
