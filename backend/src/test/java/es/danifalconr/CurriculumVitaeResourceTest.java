package es.danifalconr;

import es.danifalconr.model.AcademicStudy;
import es.danifalconr.model.WorkExperience;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CurriculumVitaeResourceTest {

    @BeforeEach
    @TestTransaction
    void setUp() {
        WorkExperience workExperience = new WorkExperience();
        workExperience.company = "test company";
        workExperience.description = "test description";
        workExperience.current = false;
        workExperience.startDate = "01/01/2021";
        workExperience.endDate = "31/12/2021";
        workExperience.persist();

        AcademicStudy academicStudy = new AcademicStudy();
        academicStudy.titleName = "test title name";
        academicStudy.schoolName = "test school name";
        academicStudy.persist();
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