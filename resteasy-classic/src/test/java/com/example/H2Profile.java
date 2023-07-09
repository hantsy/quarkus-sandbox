package com.example;

import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.List;
import java.util.Map;

public class H2Profile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
//                "quarkus.datasource.db-kind", "h2",
//                "quarkus.datasource.jdbc.url", "jdbc:h2:tcp://localhost/mem:test",
//                "quarkus.datasource.jdbc.driver", "org.h2.Driver",
//                "quarkus.hibernate-orm.database.generation", "drop-and-create",
//                "quarkus.hibernate-orm.log.sql", "true"
        );
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return List.of(new TestResourceEntry(H2DatabaseTestResource.class));
    }

    @Override
    public String getConfigProfile() {
        return "h2";
    }
}
