package es.danifalconr.infrastructure.rest.exception;

import es.danifalconr.domain.exception.InvalidTokenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException> {

    @Override
    public Response toResponse(InvalidTokenException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"error\":\"" + exception.getMessage() + "\"}")
                .build();
    }
}
