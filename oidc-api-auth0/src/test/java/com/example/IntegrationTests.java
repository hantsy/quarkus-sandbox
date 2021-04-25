package com.example;

import com.example.web.CreatePostCommand;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class IntegrationTests {

    private String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlYzM1lvNzk5cC1XeFI2NHpJZ29QMyJ9.eyJpc3MiOiJodHRwczovL2Rldi1lc2U4MjQxYi51cy5hdXRoMC5jb20vIiwic3ViIjoiSUVYVjJNYkFpdUVrVjBKN3VmSDBCcXEyYTJZSUYzaDFAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vaGFudHN5LmdpdGh1Yi5pby9hcGkiLCJpYXQiOjE2MTkzNDAyODUsImV4cCI6MTYxOTQyNjY4NSwiYXpwIjoiSUVYVjJNYkFpdUVrVjBKN3VmSDBCcXEyYTJZSUYzaDEiLCJzY29wZSI6InJlYWQ6cG9zdHMgd3JpdGU6cG9zdHMgZGVsZXRlOnBvc3RzIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.pHoj6txIAkS14QCbRJ7azp6JjfUwaT2YZrznW4CmaKoYfS_Ibi0pg-fkIhgbvTHge_2mZSYhNn76lJA8IM2A0YPRffCYmNRMXhfuhjjP1QyCdiz6u6A8nKc40cN9SG573oOC3-qHaw5jROANRyZPpAY8MEZw3HSQGYAtJ6XY_F-MoJwILv56Ah2paqcU-qhCsX5XHscxI5e1xPj3AygBOHlrKZDZMbffMZK_m3nDP5iF_I7EKsIzCS7SAiZJrSJvFSyMHz2iCkVjnB1kX8FZFItTg7YfdF2D7of2ekKR43haljoaYEtXKfoiQAdThhzHCbZ0zyrFH9zLIicxqjWDaQ";

    @BeforeAll
    public static void setupAll() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void testBearerTokenWithoutToken() {
        //@formatter:off
        given()
            .contentType(ContentType.JSON)
            .body(new CreatePostCommand("test title", "test content"))
        .when()
            .post("/posts")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
        //@formatter:on
    }

    //  From Test tab in your APIs.
//
// curl --request POST \
//         >   --url https://dev-ese8241b.us.auth0.com/oauth/token \
//         >   --header 'content-type: application/json' \
//         >   --data '{"client_id":"XXX","client_secret":"XXX","audience":"https://hantsy.github.io/api","grant_type":"client_credentials"}'
//         {"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlYzM1lvNzk5cC1XeFI2NHpJZ29QMyJ9.eyJpc3MiOiJodHRwczovL2Rldi1lc2U4MjQxYi51cy5hdXRoMC5jb20vIiwic3ViIjoiSUVYVjJNYkFpdUVrVjBKN3VmSDBCcXEyYTJZSUYzaDFAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vaGFudHN5LmdpdGh1Yi5pby9hcGkiLCJpYXQiOjE2MTkzNDAyODUsImV4cCI6MTYxOTQyNjY4NSwiYXpwIjoiSUVYVjJNYkFpdUVrVjBKN3VmSDBCcXEyYTJZSUYzaDEiLCJzY29wZSI6InJlYWQ6cG9zdHMgd3JpdGU6cG9zdHMgZGVsZXRlOnBvc3RzIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.pHoj6txIAkS14QCbRJ7azp6JjfUwaT2YZrznW4CmaKoYfS_Ibi0pg-fkIhgbvTHge_2mZSYhNn76lJA8IM2A0YPRffCYmNRMXhfuhjjP1QyCdiz6u6A8nKc40cN9SG573oOC3-qHaw5jROANRyZPpAY8MEZw3HSQGYAtJ6XY_F-MoJwILv56Ah2paqcU-qhCsX5XHscxI5e1xPj3AygBOHlrKZDZMbffMZK_m3nDP5iF_I7EKsIzCS7SAiZJrSJvFSyMHz2iCkVjnB1kX8FZFItTg7YfdF2D7of2ekKR43haljoaYEtXKfoiQAdThhzHCbZ0zyrFH9zLIicxqjWDaQ","scope":"read:posts write:posts delete:posts","expires_in":86400,"token_type":"Bearer"}
    @Test
    public void testBearerToken() {

        //@formatter:off
        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(new CreatePostCommand("test title", "test content"))
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .header("Location", notNullValue());
        //@formatter:on
    }
}