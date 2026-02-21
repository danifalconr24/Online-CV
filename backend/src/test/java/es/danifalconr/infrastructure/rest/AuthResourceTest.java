package es.danifalconr.infrastructure.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AuthResourceTest {

    // ─── POST /auth/login ───────────────────────────────────────────────────────

    @Test
    void login_withValidCredentials_returnsTokenResponse() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "admin", "password", "admin123"))
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("tokenType", equalTo("Bearer"))
                .body("expiresIn", equalTo(3600));
    }

    @Test
    void login_withInvalidPassword_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "admin", "password", "wrongpassword"))
                .when().post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    void login_withInvalidUsername_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "unknown", "password", "admin123"))
                .when().post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    void login_withMissingFields_returnsBadRequest() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/auth/login")
                .then()
                .statusCode(400);
    }

    // ─── POST /auth/refresh ─────────────────────────────────────────────────────

    @Test
    void refresh_withValidToken_returnsNewTokenResponse() {
        String accessToken = given()
                .contentType(ContentType.JSON)
                .body(Map.of("username", "admin", "password", "admin123"))
                .when().post("/auth/login")
                .then().statusCode(200)
                .extract().path("accessToken");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .when().post("/auth/refresh")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("tokenType", equalTo("Bearer"));
    }

    @Test
    void refresh_withNoAuthorizationHeader_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .when().post("/auth/refresh")
                .then()
                .statusCode(401);
    }

    @Test
    void refresh_withInvalidToken_returnsUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer this.is.not.a.valid.token")
                .when().post("/auth/refresh")
                .then()
                .statusCode(401);
    }
}
