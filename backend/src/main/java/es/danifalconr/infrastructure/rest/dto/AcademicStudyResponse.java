package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.AcademicStudy;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record AcademicStudyResponse(
        @Schema(description = "Unique identifier") Long id,
        @Schema(description = "Name of the school or institution") String schoolName,
        @Schema(description = "Degree or title obtained") String titleName
) {

    public static AcademicStudyResponse fromDomain(AcademicStudy model) {
        return new AcademicStudyResponse(model.id(), model.schoolName(), model.titleName());
    }
}
