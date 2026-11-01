package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;
import io.smallrye.reactive.messaging.memory.TestingConnector;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProfile implements QuarkusTestProfile {
    public InMemoryProfile() {
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> config = new HashMap<>();
        config.put("quarkus.rabbitmq.devservices.enabled", "false");
        config.putAll(TestingConnector.switchIncomingChannelsToTesting("messages"));
        config.putAll(TestingConnector.switchOutgoingChannelsToTesting("send", "messages-stream"));
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