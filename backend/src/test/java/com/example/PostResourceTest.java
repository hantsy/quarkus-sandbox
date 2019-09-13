package com.example;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class PostResourceTest {

    @Test
    public void getNoneExistedPost_shouldReturn404() {
        given()
                .when().get("/posts/nonexisted")
                .then()
                .statusCode(404);
    }

    @Test
    public void testPostsEndpoint() {
        given()
                .when().get("/posts")
                .then()
                .statusCode(200)
                .body(
                        "$.size()", is(2),
                        "title", containsInAnyOrder("Hello Quarkus", "Hello Again, Quarkus"),
                        "content", containsInAnyOrder("My first post of Quarkus", "My second post of Quarkus")
                );
    }


}