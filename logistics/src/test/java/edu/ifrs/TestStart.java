package edu.ifrs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;


@QuarkusTest
public class TestStart {

    @Test
    public void testGetVehicles() {
        given()
          .when().get("/logistics/getVehicle/1")
          .then()
             .statusCode(200)
             .body("loads.weight[0]", equalTo(150))
             .body("loads.weight[1]", equalTo(200))
             .body("maximumWeightLimit", equalTo(400));
    }
}
