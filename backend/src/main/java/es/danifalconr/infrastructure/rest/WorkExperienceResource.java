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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/work-experiences")
@Tag(name = "Work Experiences")
public class WorkExperienceResource {

    private final WorkExperienceService workExperienceService;

    public WorkExperienceResource(WorkExperienceService workExperienceService) {
        this.workExperienceService = workExperienceService;
    }

    @POST
    @RolesAllowed("admin")
    @Operation(summary = "Create a work experience")
    @APIResponse(responseCode = "200", description = "Work experience created")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    public WorkExperienceResponse createWorkExperience(@NotNull @Valid WorkExperienceRequest request) {
        return WorkExperienceResponse.fromDomain(workExperienceService.create(request.toDomain()));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Update a work experience")
    @APIResponse(responseCode = "200", description = "Work experience updated")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Not found")
    public WorkExperienceResponse updateWorkExperience(@PathParam("id") Long id, @NotNull @Valid WorkExperienceRequest request) {
        return WorkExperienceResponse.fromDomain(workExperienceService.update(id, request.toDomain()));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Delete a work experience")
    @APIResponse(responseCode = "204", description = "Work experience deleted")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Not found")
    public Response deleteWorkExperience(@PathParam("id") Long id) {
        workExperienceService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @PermitAll
    @Operation(summary = "List all work experiences")
    @APIResponse(responseCode = "200", description = "List of work experiences")
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
    @Operation(summary = "Upload company logo")
    @APIResponse(responseCode = "200", description = "Logo uploaded")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Not found")
    public WorkExperienceResponse uploadLogo(@PathParam("id") Long id, @RestForm("file") FileUpload file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.uploadedFile());
        return WorkExperienceResponse.fromDomain(workExperienceService.updateLogo(id, bytes));
    }
}
