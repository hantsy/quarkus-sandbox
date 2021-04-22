package com.example;

import com.example.domain.Post;
import com.example.web.PostResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
@TestProfile(PropertiesFileEmbeddedUsersProfile.class)
public class TestSecurityLazyAuthTest {


    @Test
    @TestSecurity(authorizationEnabled = false)
    public void testGetAllPostsWithoutAuth() {
        //@formatter:off
        given()
            .accept(ContentType.JSON)
        .when()
            .get("")
        .then()
            .statusCode(200);
        //@formatter:on
    }

    @Test
    //@TestSecurity(authorizationEnabled = false)
    public void testCreatPostsWithoutAuth() {
        //@formatter:off
        given()
            .body(Post.builder().title("test title").content("test content").build())
            .contentType(ContentType.JSON)
        .when()
            .post("")
        .then()
            .statusCode(401);
        //@formatter:on
    }
//TODO: SecurityAttribute is not available in the current version.
//
//    @Test
//    @TestSecurity(user = "userJwt", roles = "viewer", attributes = {
//            @SecurityAttribute(key = "claim.email", value = "user@gmail.com")
//    })
//    public void testJwtWithDummyUser() {
//        RestAssured.when().get("test-security-jwt").then()
//                .body(is("userJwt:viewer:user@gmail.com"));
//    }

}