package es.danifalconr.application;

import es.danifalconr.domain.exception.InvalidCredentialsException;
import es.danifalconr.domain.exception.InvalidTokenException;
import es.danifalconr.domain.model.AuthToken;
import es.danifalconr.domain.port.out.TokenService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class AuthServiceTest {

    @InjectMock
    TokenService tokenService;

    @Inject
    AuthService authService;

    @Test
    void login_withValidCredentials_returnsAuthToken() {
        when(tokenService.buildToken("admin")).thenReturn("test.jwt.token");

        AuthToken result = authService.login("admin", "admin123");

        assertEquals("test.jwt.token", result.accessToken());
        assertEquals("Bearer", result.tokenType());
        assertEquals(3600L, result.expiresIn());
        verify(tokenService).buildToken("admin");
    }

    @Test
    void login_withWrongPassword_throwsInvalidCredentialsException() {
        assertThrows(InvalidCredentialsException.class,
                () -> authService.login("admin", "wrongpassword"));
        verifyNoInteractions(tokenService);
    }

    @Test
    void login_withWrongUsername_throwsInvalidCredentialsException() {
        assertThrows(InvalidCredentialsException.class,
                () -> authService.login("unknown", "admin123"));
        verifyNoInteractions(tokenService);
    }

    @Test
    void refresh_withValidBearerHeader_returnsNewAuthToken() {
        when(tokenService.extractSubject("valid.raw.token")).thenReturn("admin");
        when(tokenService.buildToken("admin")).thenReturn("refreshed.jwt.token");

        AuthToken result = authService.refresh("Bearer valid.raw.token");

        assertEquals("refreshed.jwt.token", result.accessToken());
        assertEquals("Bearer", result.tokenType());
        verify(tokenService).extractSubject("valid.raw.token");
        verify(tokenService).buildToken("admin");
    }

    @Test
    void refresh_withNullHeader_throwsInvalidTokenException() {
        assertThrows(InvalidTokenException.class,
                () -> authService.refresh(null));
        verifyNoInteractions(tokenService);
    }

    @Test
    void refresh_withoutBearerPrefix_throwsInvalidTokenException() {
        assertThrows(InvalidTokenException.class,
                () -> authService.refresh("Token some-value"));
        verifyNoInteractions(tokenService);
    }

    @Test
    void refresh_whenTokenServiceThrowsOnParse_propagatesInvalidTokenException() {
        when(tokenService.extractSubject("expired.token"))
                .thenThrow(new InvalidTokenException("Invalid or expired token"));

        assertThrows(InvalidTokenException.class,
                () -> authService.refresh("Bearer expired.token"));
    }
}
