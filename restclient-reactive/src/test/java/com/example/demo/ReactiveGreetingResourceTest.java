package com.example.demo;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ReactiveGreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello-resteasy-reactive")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy Reactive"));
    }

}