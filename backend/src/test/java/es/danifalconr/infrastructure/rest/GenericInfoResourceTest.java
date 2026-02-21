package es.danifalconr.infrastructure.rest;

import es.danifalconr.application.GenericInfoService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class GenericInfoResourceTest {

    private static final String BASE_PATH = "/v1/curriculum-vitae/generic-info";

    @Inject
    GenericInfoService genericInfoService;

    // ─── GET ────────────────────────────────────────────────────────────────────

    @Test
    void getGenericInfo_isPublic_returnsOk() {
        given()
                .when().get(BASE_PATH)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("aboutMe", notNullValue());
    }

    // ─── PUT ────────────────────────────────────────────────────────────────────

    @Test
    void updateGenericInfo_withoutAuth_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("aboutMe", "Updated bio"))
                .when().put(BASE_PATH + "/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void updateGenericInfo_withNonExistentId_returnsNotFound() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("aboutMe", "Updated bio"))
                .when().put(BASE_PATH + "/999999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void updateGenericInfo_withValidId_returnsUpdatedInfo() {
        Long id = genericInfoService.getLatest().id();

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("aboutMe", "Updated about me content"))
                .when().put(BASE_PATH + "/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.intValue()))
                .body("aboutMe", equalTo("Updated about me content"));
    }
}
