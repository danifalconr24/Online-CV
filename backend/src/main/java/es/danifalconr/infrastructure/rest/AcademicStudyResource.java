package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.AcademicStudyService;
import es.danifalconr.infrastructure.rest.dto.AcademicStudyRequest;
import es.danifalconr.infrastructure.rest.dto.AcademicStudyResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/academic-studies")
public class AcademicStudyResource {

    private final AcademicStudyService academicStudyService;

    public AcademicStudyResource(AcademicStudyService academicStudyService) {
        this.academicStudyService = academicStudyService;
    }

    @POST
    @RolesAllowed("admin")
    public AcademicStudyResponse createAcademicStudy(@NotNull @Valid AcademicStudyRequest request) {
        return AcademicStudyResponse.fromDomain(academicStudyService.create(request.toDomain()));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public AcademicStudyResponse updateAcademicStudy(@PathParam("id") Long id, @NotNull @Valid AcademicStudyRequest request) {
        return AcademicStudyResponse.fromDomain(academicStudyService.update(id, request.toDomain()));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteAcademicStudy(@PathParam("id") Long id) {
        academicStudyService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @PermitAll
    public List<AcademicStudyResponse> getAcademicStudies() {
        return academicStudyService.getAll()
                .stream()
                .map(AcademicStudyResponse::fromDomain)
                .toList();
    }
}
