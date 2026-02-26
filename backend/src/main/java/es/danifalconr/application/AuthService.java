package es.danifalconr.application;

import es.danifalconr.domain.exception.InvalidCredentialsException;
import es.danifalconr.domain.exception.InvalidTokenException;
import es.danifalconr.domain.model.AuthToken;
import es.danifalconr.domain.port.out.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AuthService {

    private final TokenService tokenService;
    private final String adminUsername;
    private final String adminPassword;
    private final long jwtDurationSeconds;

    @Inject
    public AuthService(TokenService tokenService,
                       @ConfigProperty(name = "app.admin.username") String adminUsername,
                       @ConfigProperty(name = "app.admin.password") String adminPassword,
                       @ConfigProperty(name = "app.jwt.duration") long jwtDurationSeconds) {
        this.tokenService = tokenService;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.jwtDurationSeconds = jwtDurationSeconds;
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
