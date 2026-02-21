package es.danifalconr.infrastructure.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AcademicStudyResourceTest {

    private static final String BASE_PATH = "/v1/curriculum-vitae/academic-studies";

    private static final Map<String, String> VALID_REQUEST = Map.of(
            "schoolName", "MIT",
            "titleName", "Computer Science"
    );

    // ─── GET ────────────────────────────────────────────────────────────────────

    @Test
    void getAllAcademicStudies_isPublic_returnsOk() {
        given()
                .when().get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    void getAllAcademicStudies_returnsListWithExpectedFields() {
        given()
                .when().get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("[0].id", notNullValue())
                .body("[0].schoolName", notNullValue())
                .body("[0].titleName", notNullValue());
    }

    // ─── POST ───────────────────────────────────────────────────────────────────

    @Test
    void createAcademicStudy_withoutAuth_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void createAcademicStudy_withAuth_returnsCreatedStudy() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("schoolName", equalTo("MIT"))
                .body("titleName", equalTo("Computer Science"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void createAcademicStudy_withMissingRequiredFields_returnsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("schoolName", "MIT"))
                .when().post(BASE_PATH)
                .then()
                .statusCode(400);
    }

    // ─── PUT ────────────────────────────────────────────────────────────────────

    @Test
    void updateAcademicStudy_withoutAuth_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().put(BASE_PATH + "/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void updateAcademicStudy_withNonExistentId_returnsNotFound() {
        given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().put(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void updateAcademicStudy_withValidId_returnsUpdatedStudy() {
        int id = given()
                .contentType(ContentType.JSON)
                .body(VALID_REQUEST)
                .when().post(BASE_PATH)
                .then().statusCode(200)
                .extract().path("id");

        Map<String, String> updateRequest = Map.of(
                "schoolName", "Stanford",
                "titleName", "Software Engineering"
        );

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when().put(BASE_PATH + "/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("schoolName", equalTo("Stanford"))
                .body("titleName", equalTo("Software Engineering"));
    }

    // ─── DELETE ─────────────────────────────────────────────────────────────────

    @Test
    void deleteAcademicStudy_withoutAuth_returnsUnauthorized() {
        given()
                .when().delete(BASE_PATH + "/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void deleteAcademicStudy_withNonExistentId_returnsNotFound() {
        given()
                .when().delete(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void deleteAcademicStudy_withValidId_returnsNoContent() {
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
