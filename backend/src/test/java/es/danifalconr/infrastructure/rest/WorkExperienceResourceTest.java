package es.danifalconr.infrastructure.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class WorkExperienceResourceTest {

    private static final String BASE_PATH = "/v1/curriculum-vitae/work-experiences";

    private static final Map<String, Object> VALID_REQUEST = Map.of(
            "startDate", "01/01/2021",
            "endDate", "31/12/2021",
            "current", false,
            "company", "ACME Corp",
            "description", "Software Engineer role"
    );

    // ─── GET ────────────────────────────────────────────────────────────────────

    @Test
    void getAllWorkExperiences_isPublic_returnsOk() {
        given()
                .when().get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void getAllWorkExperiences_returnsListWithExpectedFields() {
        given()
                .when().get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("[0].id", notNullValue())
                .body("[0].company", notNullValue())
                .body("[0].startDate", notNullValue())
                .body("[0].description", notNullValue());
    }

    // ─── POST ───────────────────────────────────────────────────────────────────

    @Test
    void createWorkExperience_withoutAuth_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void createWorkExperience_withAuth_returnsCreatedExperience() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("company", equalTo("ACME Corp"))
                .body("startDate", equalTo("01/01/2021"))
                .body("description", equalTo("Software Engineer role"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void createWorkExperience_withMissingRequiredFields_returnsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("endDate", "31/12/2021"))
                .when().post(BASE_PATH)
                .then()
                .statusCode(400);
    }

    // ─── PUT ────────────────────────────────────────────────────────────────────

    @Test
    void updateWorkExperience_withoutAuth_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().put(BASE_PATH + "/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void updateWorkExperience_withNonExistentId_returnsNotFound() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().put(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void updateWorkExperience_withValidId_returnsUpdatedExperience() {
        int id = given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then().statusCode(200)
                .extract().path("id");

        Map<String, Object> updateRequest = Map.of(
                "startDate", "01/01/2021",
                "endDate", "31/12/2022",
                "current", false,
                "company", "Updated Corp",
                "description", "Senior Engineer"
        );

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when().put(BASE_PATH + "/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("company", equalTo("Updated Corp"))
                .body("description", equalTo("Senior Engineer"));
    }

    // ─── PUT /{id}/logo ──────────────────────────────────────────────────────────

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void uploadLogo_withValidId_returnsUpdatedWorkExperience() {
        int id = given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then().statusCode(200)
                .extract().path("id");

        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", "logo.png", "fake-image-bytes".getBytes(), "image/png")
                .when().put(BASE_PATH + "/" + id + "/logo")
                .then()
                .statusCode(200)
                .body("companyLogo", notNullValue());
    }

    @Test
    void uploadLogo_withoutAuth_returnsUnauthorized() {
        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", "logo.png", "fake-image-bytes".getBytes(), "image/png")
                .when().put(BASE_PATH + "/1/logo")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void uploadLogo_withNonExistentId_returnsNotFound() {
        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", "logo.png", "fake-image-bytes".getBytes(), "image/png")
                .when().put(BASE_PATH + "/99999/logo")
                .then()
                .statusCode(404);
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────────

    @Test
    void deleteWorkExperience_withoutAuth_returnsUnauthorized() {
        given()
                .when().delete(BASE_PATH + "/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void deleteWorkExperience_withNonExistentId_returnsNotFound() {
        given()
                .when().delete(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void deleteWorkExperience_withValidId_returnsNoContent() {
        int id = given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then().statusCode(200)
                .extract().path("id");

        given()
                .when().delete(BASE_PATH + "/" + id)
                .then()
                .statusCode(204);
    }
}
