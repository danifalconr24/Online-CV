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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/curriculum-vitae/academic-studies")
@Tag(name = "Academic Studies")
public class AcademicStudyResource {

    private final AcademicStudyService academicStudyService;

    public AcademicStudyResource(AcademicStudyService academicStudyService) {
        this.academicStudyService = academicStudyService;
    }

    @POST
    @RolesAllowed("admin")
    @Operation(summary = "Create an academic study")
    @APIResponse(responseCode = "200", description = "Academic study created")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    public AcademicStudyResponse createAcademicStudy(@NotNull @Valid AcademicStudyRequest request) {
        return AcademicStudyResponse.fromDomain(academicStudyService.create(request.toDomain()));
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Update an academic study")
    @APIResponse(responseCode = "200", description = "Academic study updated")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Not found")
    public AcademicStudyResponse updateAcademicStudy(@PathParam("id") Long id, @NotNull @Valid AcademicStudyRequest request) {
        return AcademicStudyResponse.fromDomain(academicStudyService.update(id, request.toDomain()));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Delete an academic study")
    @APIResponse(responseCode = "204", description = "Academic study deleted")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Not found")
    public Response deleteAcademicStudy(@PathParam("id") Long id) {
        academicStudyService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @PermitAll
    @Operation(summary = "List all academic studies")
    @APIResponse(responseCode = "200", description = "List of academic studies")
    public List<AcademicStudyResponse> getAcademicStudies() {
        return academicStudyService.getAll()
                .stream()
                .map(AcademicStudyResponse::fromDomain)
                .toList();
    }
}
