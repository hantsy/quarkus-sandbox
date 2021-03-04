package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class H2Profile implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.datasource.jdbc.url", "jdbc:h2:tcp://localhost/mem:test",
                "quarkus.datasource.jdbc.driver", "org.h2.Driver",
                "quarkus.hibernate-orm.database.generation", "drop-and-create",
                "quarkus.hibernate-orm.log.sql", "true"
        );
    }

    @Override
    public String getConfigProfile() {
        return "h2";
    }
}
