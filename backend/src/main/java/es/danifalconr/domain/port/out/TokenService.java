package es.danifalconr.domain.port.out;

public interface TokenService {

    String buildToken(String subject);

    String extractSubject(String rawToken);
}
