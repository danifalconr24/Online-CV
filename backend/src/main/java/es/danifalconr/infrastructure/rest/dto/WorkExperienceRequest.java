package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.WorkExperience;
import jakarta.validation.constraints.NotNull;

public record WorkExperienceRequest(
        @NotNull String startDate,
        String endDate,
        Boolean current,
        @NotNull String company,
        @NotNull String description
) {

    public WorkExperience toDomain() {
        return new WorkExperience(null, startDate, endDate, current, company, description);
    }
}
