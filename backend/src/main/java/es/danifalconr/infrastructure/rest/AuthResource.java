package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.AuthService;
import es.danifalconr.infrastructure.rest.dto.LoginRequest;
import es.danifalconr.infrastructure.rest.dto.LoginResponse;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthResource {

    private final AuthService authService;
    private final JWTParser jwtParser;

    public AuthResource(AuthService authService, JWTParser jwtParser) {
        this.authService = authService;
        this.jwtParser = jwtParser;
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@NotNull @Valid LoginRequest request) {
        String token = authService.login(request.username(), request.password());
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Invalid credentials\"}")
                    .build();
        }
        return Response.ok(new LoginResponse(token, "Bearer", authService.getJwtDurationSeconds())).build();
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public Response refresh(@HeaderParam("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Missing or invalid Authorization header\"}")
                    .build();
        }
        String token = authorizationHeader.substring("Bearer ".length());
        try {
            JsonWebToken jwt = jwtParser.parse(token);
            String newToken = authService.refresh(jwt.getSubject());
            return Response.ok(new LoginResponse(newToken, "Bearer", authService.getJwtDurationSeconds())).build();
        } catch (ParseException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Invalid or expired token\"}")
                    .build();
        }
    }
}
