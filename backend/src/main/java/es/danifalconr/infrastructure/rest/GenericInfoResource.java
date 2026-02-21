package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.GenericInfoService;
import es.danifalconr.infrastructure.rest.dto.GenericInfoRequest;
import es.danifalconr.infrastructure.rest.dto.GenericInfoResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/generic-info")
public class GenericInfoResource {

    private final GenericInfoService genericInfoService;

    public GenericInfoResource(GenericInfoService genericInfoService) {
        this.genericInfoService = genericInfoService;
    }

    @GET
    @PermitAll
    public GenericInfoResponse getGenericInfo() {
        return GenericInfoResponse.fromDomain(genericInfoService.getLatest());
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public GenericInfoResponse updateGenericInfo(@PathParam("id") Long id, @NotNull @Valid GenericInfoRequest request) {
        return GenericInfoResponse.fromDomain(genericInfoService.update(id, request.toDomain()));
    }
}
