package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class InMemoryProfile implements QuarkusTestProfile {
    public InMemoryProfile() {
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.kafka.devservices.enabled", "false",
                // see: https://github.com/quarkusio/quarkus/issues/40317#issuecomment-2082911239
                "quarkus.messaging.kafka.serializer-autodetection.enabled", "false",
                "mp.messaging.outgoing.send.connector", "smallrye-in-memory",
                "mp.messaging.incoming.messages.connector", "smallrye-in-memory",
                "mp.messaging.outgoing.messages-stream.connector", "smallrye-in-memory"
        );
    }

    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }


    @Override
    public boolean runMainMethod() {
        return false;
    }

    @Override
    public boolean disableApplicationLifecycleObservers() {
        return true;
    }
}