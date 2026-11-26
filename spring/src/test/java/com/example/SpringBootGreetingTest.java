package com.example;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * A Spring Boot style test running on Quarkus.
 *
 * {@code @SpringBootTest} is an alias for {@code @QuarkusTest}: it boots the
 * Quarkus runtime and the CDI container, not a Spring Application Context.
 * The Spring annotations are only read for their metadata, so the familiar
 * Spring Boot import keeps working unchanged.
 */
@SpringBootTest
public class SpringBootGreetingTest {

    @Autowired
    GreetingProperties properties;

    @Test
    public void testGreetingProperties() {
        assertThat(properties.getText()).isEqualTo("Message from properties file.");
    }

    @Test
    public void testGreetingEndpoint() {
        given()
                .accept(ContentType.TEXT)
                .when()
                .get("/greeting?name=Quarkus")
                .then()
                .statusCode(200)
                .body(containsString("Message from properties file., Quarkus"));
    }
}
