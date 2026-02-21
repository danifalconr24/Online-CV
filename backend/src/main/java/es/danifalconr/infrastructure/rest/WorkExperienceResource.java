package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.WorkExperienceService;
import es.danifalconr.infrastructure.rest.dto.WorkExperienceRequest;
import es.danifalconr.infrastructure.rest.dto.WorkExperienceResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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
    public WorkExperienceResponse createWorkExperience(@NotNull @Valid WorkExperienceRequest request) {
        return WorkExperienceResponse.fromDomain(workExperienceService.create(request.toDomain()));
    }

    @GET
    public List<WorkExperienceResponse> getWorkExperiences() {
        return workExperienceService.getAll()
                .stream()
                .map(WorkExperienceResponse::fromDomain)
                .toList();
    }
}
