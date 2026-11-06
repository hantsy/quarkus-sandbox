package com.example;

import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class H2Profile implements QuarkusTestProfile {

    // get the h2 properties from application-h2.properties instead
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
    public Set<Class<?>> getEnabledAlternatives() {
        return Collections.emptySet();
    }

    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }

    @Override
    public Set<String> tags() {
        return Collections.emptySet();
    }

    @Override
    public String[] commandLineParameters() {
        return new String[0];
    }

    @Override
    public boolean runMainMethod() {
        return false;
    }

    @Override
    public boolean disableApplicationLifecycleObservers() {
        return false;
    }

    @Override
    public String getConfigProfile() {
        return "h2";
    }
}