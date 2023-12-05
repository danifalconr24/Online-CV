package es.danifalconr;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CurriculumVitaeResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/v1/curriculum-vitae/latest")
          .then()
             .statusCode(200);
    }

}