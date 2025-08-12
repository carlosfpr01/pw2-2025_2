package dev.ifrs;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class StartTest {
    @Test
    void testConversionKmMiles() {
        given()
            .contentType("application/x-www-form-urlencoded")
            // 50 quilômetros por hora
            .formParam("km", "50")
        .when()
            .post("/Conversion/km-to-miles/")
        .then()
            .contentType("text/plain")
            .statusCode(200)
            .body(is("31.06855"));
    }

    @Test
    void testConversionKnotsKm() {
        given()
            .contentType(ContentType.HTML)
        .when()
            .contentType(ContentType.JSON)
            .get("/Conversion/knots-to-km/1")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

}