package es.danifalconr.domain.model;

public record WorkExperience(
        Long id,
        String startDate,
        String endDate,
        Boolean current,
        String company,
        String description
) {
}
