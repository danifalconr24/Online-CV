package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.AuthService;
import es.danifalconr.domain.model.AuthToken;
import es.danifalconr.infrastructure.rest.dto.LoginRequest;
import es.danifalconr.infrastructure.rest.dto.LoginResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
@Tag(name = "Authentication")
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @PermitAll
    @Operation(summary = "Login with credentials")
    @APIResponse(responseCode = "200", description = "Login successful")
    @APIResponse(responseCode = "401", description = "Invalid credentials")
    public LoginResponse login(@NotNull @Valid LoginRequest request) {
        AuthToken token = authService.login(request.username(), request.password());
        return new LoginResponse(token.accessToken(), token.tokenType(), token.expiresIn());
    }

    @POST
    @Path("/refresh")
    @PermitAll
    @Operation(summary = "Refresh access token")
    @APIResponse(responseCode = "200", description = "Token refreshed")
    @APIResponse(responseCode = "401", description = "Invalid or expired token")
    public LoginResponse refresh(@HeaderParam("Authorization") String authorizationHeader) {
        AuthToken token = authService.refresh(authorizationHeader);
        return new LoginResponse(token.accessToken(), token.tokenType(), token.expiresIn());
    }
}
