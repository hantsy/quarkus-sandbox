package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import java.util.List;

import java.util.Map;

public class MockOidcServerProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.oidc.auth-server-url", "${keycloak.url}/realms/quarkus/",
                "quarkus.oidc.client-id", "quarkus-service-app",
                "quarkus.oidc.credentials.secret", "secret",
                "quarkus.oidc.token.audience", "https://server.example.com",
                "quarkus.oidc.token.principal-claim", "username",
                //"quarkus.oidc.tls.verification", "none",
                "smallrye.jwt.sign.key-location","privateKey.jwk"
        );
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return List.of(new TestResourceEntry(OidcWiremockTestResource.class));
    }
    
    @Override
    public String getConfigProfile() {
        return "mock-oidc-server";
    }

}
