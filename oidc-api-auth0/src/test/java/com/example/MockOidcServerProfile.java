package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class MockOidcServerProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.oidc.auth-server-url", "${keycloak.url}/realms/quarkus/",
                "quarkus.oidc.client-id", "quarkus-service-app",
                "quarkus.oidc.credentials.secret", "secret",
                "quarkus.oidc.token.principal-claim", "email",
                "quarkus.oidc.tls.verification", "none",
                "smallrye.jwt.sign.key-location","false"
        );
    }

    @Override
    public String getConfigProfile() {
        return "mock-oidc-server";
    }
}
