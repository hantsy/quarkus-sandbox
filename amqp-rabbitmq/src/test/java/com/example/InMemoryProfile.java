package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class InMemoryProfile implements QuarkusTestProfile {
    public InMemoryProfile() {
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "mp.messaging.outgoing.send.connector","smallrye-in-memory",
                "mp.messaging.incoming.messages.connector","smallrye-in-memory",
                "mp.messaging.outgoing.data-stream.connector","smallrye-in-memory"
        );
    }

    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }


    @Override
    public boolean runMainMethod() {
        return true;
    }

    @Override
    public boolean disableApplicationLifecycleObservers() {
        return true;
    }
}