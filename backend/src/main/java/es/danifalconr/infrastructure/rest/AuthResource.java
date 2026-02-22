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

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @PermitAll
    public LoginResponse login(@NotNull @Valid LoginRequest request) {
        AuthToken token = authService.login(request.username(), request.password());
        return new LoginResponse(token.accessToken(), token.tokenType(), token.expiresIn());
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public LoginResponse refresh(@HeaderParam("Authorization") String authorizationHeader) {
        AuthToken token = authService.refresh(authorizationHeader);
        return new LoginResponse(token.accessToken(), token.tokenType(), token.expiresIn());
    }
}
