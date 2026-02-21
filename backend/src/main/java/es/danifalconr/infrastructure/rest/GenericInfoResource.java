package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.GenericInfoService;
import es.danifalconr.infrastructure.rest.dto.GenericInfoResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    public GenericInfoResponse getGenericInfo() {
        return GenericInfoResponse.fromDomain(genericInfoService.getLatest());
    }
}
