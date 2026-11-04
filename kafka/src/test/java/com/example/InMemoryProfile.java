package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.HashMap;
import java.util.Map;

public class InMemoryProfile implements QuarkusTestProfile {
    public InMemoryProfile() {
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> config = new HashMap<>();
        config.put("quarkus.kafka.devservices.enabled", "false");
        // Do not use TestingConnector.switchXxxChannelsToTesting(...) here,
        // it also sets JVM-wide system properties (System.setProperty) which leak
        // into the following test boots and silently re-route the real broker
        // connector channels of the default profile to the testing connector.
        config.put("mp.messaging.incoming.messages.connector", "smallrye-testing");
        config.put("mp.messaging.outgoing.send.connector", "smallrye-testing");
        config.put("mp.messaging.outgoing.messages-stream.connector", "smallrye-testing");
        config.put("quarkus.messaging.kafka.serializer-autodetection.enabled", "false");
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