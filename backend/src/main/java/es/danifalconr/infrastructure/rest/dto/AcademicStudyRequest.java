package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.AcademicStudy;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record AcademicStudyRequest(
        @NotNull @Schema(description = "Name of the school or institution") String schoolName,
        @NotNull @Schema(description = "Degree or title obtained") String titleName
) {

    public AcademicStudy toDomain() {
        return new AcademicStudy(null, schoolName, titleName);
    }
}
