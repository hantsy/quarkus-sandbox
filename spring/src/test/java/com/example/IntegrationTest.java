package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class IntegrationTest {

    @Test
    public void testPostsEndpoint() {
        given()
          .when().get("/posts")
          .then()
             .statusCode(200);
    }

}