package es.danifalconr.infrastructure.rest.dto;

import es.danifalconr.domain.model.WorkExperience;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record WorkExperienceRequest(
        @NotNull @Schema(description = "Start date (e.g. 2023-01)") String startDate,
        @Schema(description = "End date, null if current") String endDate,
        @Schema(description = "Whether this is the current position") Boolean current,
        @NotNull @Schema(description = "Company name") String company,
        @NotNull @Schema(description = "Role or job description") String description,
        @Schema(description = "Base64-encoded company logo") String companyLogo
) {

    public WorkExperience toDomain() {
        return new WorkExperience(null, startDate, endDate, current, company, description, companyLogo);
    }
}
