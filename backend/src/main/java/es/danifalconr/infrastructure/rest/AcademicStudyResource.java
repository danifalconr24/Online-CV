package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.AcademicStudyService;
import es.danifalconr.infrastructure.rest.dto.AcademicStudyRequest;
import es.danifalconr.infrastructure.rest.dto.AcademicStudyResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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
    public AcademicStudyResponse createAcademicStudy(@NotNull @Valid AcademicStudyRequest request) {
        return AcademicStudyResponse.fromDomain(academicStudyService.create(request.toDomain()));
    }

    @GET
    public List<AcademicStudyResponse> getAcademicStudies() {
        return academicStudyService.getAll()
                .stream()
                .map(AcademicStudyResponse::fromDomain)
                .toList();
    }
}
