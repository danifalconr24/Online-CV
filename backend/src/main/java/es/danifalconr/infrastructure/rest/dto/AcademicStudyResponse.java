package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.AcademicStudy;

public record AcademicStudyResponse(Long id, String schoolName, String titleName) {

    public static AcademicStudyResponse fromDomain(AcademicStudy model) {
        return new AcademicStudyResponse(model.getId(), model.getSchoolName(), model.getTitleName());
    }
}
