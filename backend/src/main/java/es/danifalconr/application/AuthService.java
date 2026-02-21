package es.danifalconr.application;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Set;

@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "app.admin.username")
    String adminUsername;

    @ConfigProperty(name = "app.admin.password")
    String adminPassword;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "app.jwt.duration")
    long jwtDurationSeconds;

    public String login(String username, String password) {
        if (!adminUsername.equals(username) || !adminPassword.equals(password)) {
            return null;
        }
        return buildToken(username);
    }

    public String refresh(String username) {
        return buildToken(username);
    }

    private String buildToken(String username) {
        long now = System.currentTimeMillis() / 1000;
        return Jwt.issuer(issuer)
                .subject(username)
                .groups(Set.of("admin"))
                .issuedAt(now)
                .expiresAt(now + jwtDurationSeconds)
                .sign();
    }

    public long getJwtDurationSeconds() {
        return jwtDurationSeconds;
    }
}
