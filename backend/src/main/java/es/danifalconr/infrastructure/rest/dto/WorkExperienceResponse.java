package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.WorkExperience;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record WorkExperienceResponse(
        @Schema(description = "Unique identifier") Long id,
        @Schema(description = "Start date") String startDate,
        @Schema(description = "End date") String endDate,
        @Schema(description = "Whether this is the current position") Boolean current,
        @Schema(description = "Company name") String company,
        @Schema(description = "Role or job description") String description,
        @Schema(description = "Base64-encoded company logo") String companyLogo
) {

    public static WorkExperienceResponse fromDomain(WorkExperience model) {
        return new WorkExperienceResponse(
                model.id(),
                model.startDate(),
                model.endDate(),
                model.current(),
                model.company(),
                model.description(),
                model.companyLogo()
        );
    }
}
