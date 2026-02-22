package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.WorkExperienceService;
import es.danifalconr.domain.model.WorkExperience;
import es.danifalconr.infrastructure.rest.dto.WorkExperienceRequest;
import es.danifalconr.infrastructure.rest.dto.WorkExperienceResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
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

    @PUT
    @Path("/{id}/logo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("admin")
    public WorkExperienceResponse uploadLogo(@PathParam("id") Long id, @RestForm("file") FileUpload file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.uploadedFile());
        String base64Logo = Base64.getEncoder().encodeToString(bytes);
        WorkExperience current = workExperienceService.getAll().stream()
                .filter(w -> w.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("WorkExperience not found with id: " + id));
        WorkExperience updated = new WorkExperience(
                current.id(), current.startDate(), current.endDate(),
                current.current(), current.company(), current.description(), base64Logo);
        return WorkExperienceResponse.fromDomain(workExperienceService.update(id, updated));
    }
}
