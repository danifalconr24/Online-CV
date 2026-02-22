package es.danifalconr.application;

import es.danifalconr.domain.exception.InvalidCredentialsException;
import es.danifalconr.domain.exception.InvalidTokenException;
import es.danifalconr.domain.model.AuthToken;
import es.danifalconr.domain.port.out.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "app.admin.username")
    String adminUsername;

    @ConfigProperty(name = "app.admin.password")
    String adminPassword;

    @ConfigProperty(name = "app.jwt.duration")
    long jwtDurationSeconds;

    private final TokenService tokenService;

    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public AuthToken login(String username, String password) {
        if (!adminUsername.equals(username) || !adminPassword.equals(password)) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return new AuthToken(tokenService.buildToken(username), "Bearer", jwtDurationSeconds);
    }

    public AuthToken refresh(String bearerHeader) {
        if (bearerHeader == null || !bearerHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Missing or invalid Authorization header");
        }
        String rawToken = bearerHeader.substring("Bearer ".length());
        String subject = tokenService.extractSubject(rawToken);
        return new AuthToken(tokenService.buildToken(subject), "Bearer", jwtDurationSeconds);
    }
}
