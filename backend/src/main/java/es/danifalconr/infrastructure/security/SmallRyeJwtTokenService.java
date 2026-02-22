package es.danifalconr.infrastructure.security;

import es.danifalconr.domain.exception.InvalidTokenException;
import es.danifalconr.domain.port.out.TokenService;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Set;

@ApplicationScoped
public class SmallRyeJwtTokenService implements TokenService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "app.jwt.duration")
    long jwtDurationSeconds;

    private final JWTParser jwtParser;

    public SmallRyeJwtTokenService(JWTParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Override
    public String buildToken(String subject) {
        long now = System.currentTimeMillis() / 1000;
        return Jwt.issuer(issuer)
                .subject(subject)
                .groups(Set.of("admin"))
                .issuedAt(now)
                .expiresAt(now + jwtDurationSeconds)
                .sign();
    }

    @Override
    public String extractSubject(String rawToken) {
        try {
            JsonWebToken jwt = jwtParser.parse(rawToken);
            return jwt.getSubject();
        } catch (ParseException e) {
            throw new InvalidTokenException("Invalid or expired token");
        }
    }
}
