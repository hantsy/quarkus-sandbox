package com.example;

import io.quarkus.oidc.OidcConfigurationMetadata;
import io.quarkus.test.junit.QuarkusTestProfile;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mockito.Mockito;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PropertiesFileEmbeddedUsersProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.security.users.embedded.enabled", "true",
                "quarkus.security.users.embedded.plain-text", "true",
                "quarkus.security.users.embedded.users.alice", "password",
                "quarkus.security.users.embedded.roles.alice", "user",
                "quarkus.security.users.embedded.users.admin", "password",
                "quarkus.security.users.embedded.roles.admin", "admin",
                "quarkus.http.auth.basic", "true",
                "quarkus.oidc.enabled","false"
        );
    }

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
        return QuarkusTestProfile.super.getEnabledAlternatives();
    }

    @Override
    public String getConfigProfile() {
        return "embedded-users";
    }

    @Dependent
    static class AudienceValidatorProducer {

        @Produces
        public JsonWebToken mockJsonWebToken(){
            JsonWebToken token = Mockito.mock(JsonWebToken.class);
            when(token.getAudience()).thenReturn(Set.of("test"));
            return token;
        }

        @Produces
        public OidcConfigurationMetadata mockOidcConfigurationMetadata(){
            OidcConfigurationMetadata metadata = Mockito.mock(OidcConfigurationMetadata.class);
            when(metadata.get(anyString())).thenReturn("test");
            return metadata;
        }
    }
}
