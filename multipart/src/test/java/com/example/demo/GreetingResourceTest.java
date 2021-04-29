package com.example.demo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }
    
    @Test
    public void testUploadEndpoint() {
        given().formParam("test", "a String field")
                .multiPart("file", new File("test.csv"), "text/csv")
                .when().post("")
                .then()
                .statusCode(200).log().all(true);
    }

}