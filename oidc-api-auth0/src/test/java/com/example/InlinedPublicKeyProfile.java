package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InlinedPublicKeyProfile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.oidc.client-id", "test",
                "quarkus.oidc.public-key", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzcAl1DgYnAkpYelL4lxl" +
                        "csdX0dHj92g+pMyjzWE3nzSV8i726lTJtg5kxaJyv8epEOAiMcelFk0v+9HMOPla" +
                        "6/pgkEHE5PPyaHlgegDIzge2RQLidDwl8IWBksUkfDWjQk+JfrEPJxrES4OXUOyp" +
                        "mO/XsNIGcbZfopi1Ook7XIjBPohuiHBcp8Fw1NVzaP7EvyYzxcxoIpa4Y/knF1Sa" +
                        "FIBuMmA/lE7PHKlBqcsS1EXkyI1TBGcdH+VWhUvsDVehKSlZoUCE6XrWY3M/xzyb" +
                        "gN+C9KPln+fQZ42Fnqo6PpD++NWRo8vgWxsMsqb+nltEQADfd8CFEuLt1BvObh8N" +
                        "fQIDAQAB",
                "quarkus.oidc.token.audience", "https://service.example.com",
                "smallrye.jwt.sign.key-location", "privateKey.jwk"
        );
    }

    @Override
    public List<QuarkusTestProfile.TestResourceEntry> testResources() {
        return Collections.emptyList();
    }

    @Override
    public String getConfigProfile() {
        return "inlinedpubkey";
    }
}
