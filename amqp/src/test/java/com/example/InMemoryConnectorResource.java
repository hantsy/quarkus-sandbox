package com.example;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;

import java.util.Map;

public class InMemoryConnectorResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        InMemoryConnector.switchIncomingChannelsToInMemory("messages");
        InMemoryConnector.switchOutgoingChannelsToInMemory("send");
        InMemoryConnector.switchOutgoingChannelsToInMemory("messages-stream");
        return Map.of();
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }
}
