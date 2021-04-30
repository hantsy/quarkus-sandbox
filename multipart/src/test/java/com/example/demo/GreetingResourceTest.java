package com.example.demo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

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
//                .formParam("checked", "true")
//                .formParam("choice", "YES")
                .multiPart("file", new File("test.csv"), "text/csv")
                .when().post("")
                .then()
                .statusCode(200).log().all(true);
    }

    @Test
    public void testPostPOJOEndpoint() {
        given().body(SimplePoJo.of("a test string", true, Choice.YES, LocalDateTime.now(), null))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post("pojo")
                .then()
                .statusCode(200).log().all(true);
    }

    @Test
    public void testUpload2Endpoint() {
        given().formParam("test", "a String field")
//                .formParam("checked", "true")
//                .formParam("choice", "YES")
                .multiPart("file", new File("test.csv"), "text/csv")
                .when().post("upload2")
                .then()
                .statusCode(200).log().all(true);
    }

    @Test
    public void testUploadBinEndpoint() {
        given().body(new File("test.csv"))
                .contentType("application/octet-stream")
                .when().post("bin")
                .then()
                .statusCode(200).log().all(true);
    }


    @Test
    public void testMultiFormOutputEndpoint() {
        given()
                .when().get("/output")
                .then()
                .statusCode(200)
                .log().all(true);
    }

}