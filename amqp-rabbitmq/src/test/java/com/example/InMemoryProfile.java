package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProfile implements QuarkusTestProfile {
    public InMemoryProfile() {
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> config = new HashMap<>();
        config.put("quarkus.amqp.devservices.enabled", "false");
        config.putAll(InMemoryConnector.switchIncomingChannelsToInMemory("messages"));
        config.putAll(InMemoryConnector.switchOutgoingChannelsToInMemory("send", "messages-stream"));
        return config;
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