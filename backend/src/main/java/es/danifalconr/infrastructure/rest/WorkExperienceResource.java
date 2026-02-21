package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.WorkExperienceService;
import es.danifalconr.infrastructure.rest.dto.WorkExperienceRequest;
import es.danifalconr.infrastructure.rest.dto.WorkExperienceResponse;
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
@Path("/v1/curriculum-vitae/work-experiences")
public class WorkExperienceResource {

    private final WorkExperienceService workExperienceService;

    public WorkExperienceResource(WorkExperienceService workExperienceService) {
        this.workExperienceService = workExperienceService;
    }

    @POST
    @RolesAllowed("admin")
    public WorkExperienceResponse createWorkExperience(@NotNull @Valid WorkExperienceRequest request) {
        return WorkExperienceResponse.fromDomain(workExperienceService.create(request.toDomain()));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public WorkExperienceResponse updateWorkExperience(@PathParam("id") Long id, @NotNull @Valid WorkExperienceRequest request) {
        return WorkExperienceResponse.fromDomain(workExperienceService.update(id, request.toDomain()));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deleteWorkExperience(@PathParam("id") Long id) {
        workExperienceService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @PermitAll
    public List<WorkExperienceResponse> getWorkExperiences() {
        return workExperienceService.getAll()
                .stream()
                .map(WorkExperienceResponse::fromDomain)
                .toList();
    }
}
