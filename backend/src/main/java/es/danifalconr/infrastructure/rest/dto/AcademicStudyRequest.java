package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.AcademicStudy;
import jakarta.validation.constraints.NotNull;

public record AcademicStudyRequest(
        @NotNull String schoolName,
        @NotNull String titleName
) {

    public AcademicStudy toDomain() {
        AcademicStudy model = new AcademicStudy();
        model.setSchoolName(schoolName);
        model.setTitleName(titleName);
        return model;
    }
}
