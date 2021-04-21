package com.example;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class BearerTokenAuthorizationTest {

    @Test
    public void testBearerToken() {
        //@formatter:off
        given()
            .auth().oauth2(getAccessToken("alice", new HashSet<>(Arrays.asList("user"))))
        .when()
            .get("/posts")
        .then()
            .statusCode(200);
            // the test endpoint returns the name extracted from the injected SecurityIdentity Principal
            //.body("userName", equalTo("alice"));
        //@formatter:on
    }

    private String getAccessToken(String userName, Set<String> groups) {
        return Jwt.preferredUserName(userName)
                .claim("scope", "")
                .groups(groups)
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .jws()
                .keyId("1")
                .sign();
    }
}