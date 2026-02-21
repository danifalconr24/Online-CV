package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.WorkExperience;

public record WorkExperienceResponse(
        Long id,
        String startDate,
        String endDate,
        Boolean current,
        String company,
        String description
) {

    public static WorkExperienceResponse fromDomain(WorkExperience model) {
        return new WorkExperienceResponse(
                model.getId(),
                model.getStartDate(),
                model.getEndDate(),
                model.getCurrent(),
                model.getCompany(),
                model.getDescription()
        );
    }
}
