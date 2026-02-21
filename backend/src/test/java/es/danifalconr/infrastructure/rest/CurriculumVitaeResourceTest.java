package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.AcademicStudyService;
import es.danifalconr.application.WorkExperienceService;
import es.danifalconr.domain.model.AcademicStudy;
import es.danifalconr.domain.model.WorkExperience;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CurriculumVitaeResourceTest {

    @Inject
    WorkExperienceService workExperienceService;

    @Inject
    AcademicStudyService academicStudyService;

    @BeforeEach
    @TestTransaction
    void setUp() {
        workExperienceService.create(new WorkExperience(null, "01/01/2021", "31/12/2021", false, "test company", "test description"));
        academicStudyService.create(new AcademicStudy(null, "test school name", "test title name"));
    }

    @Test
    public void getWorkExperiences() {
        given()
          .when().get("/v1/curriculum-vitae/work-experiences")
          .then()
             .statusCode(200);
    }

    @Test
    public void getAcademicStudies() {
        given()
          .when().get("/v1/curriculum-vitae/academic-studies")
          .then()
             .statusCode(200);
    }
}
