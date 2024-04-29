package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class InMemoryProfile implements QuarkusTestProfile {
    public InMemoryProfile() {
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "quarkus.pulsar.devservices.enabled", "false",
                "mp.messaging.outgoing.send.connector","smallrye-in-memory",
                "mp.messaging.incoming.messages.connector","smallrye-in-memory",
                "mp.messaging.outgoing.data-result.connector","smallrye-in-memory"
//                "pulsar.client.serviceUrl", "",
//                "pulsar.admin.serviceUrl", ""
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