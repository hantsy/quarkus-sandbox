package com.example;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;

import java.util.Map;

public class ArtemisTestResource implements QuarkusTestResourceLifecycleManager {

    private static final GenericContainer container = new GenericContainer("quay.io/artemiscloud/activemq-artemis-broker")
            .withExposedPorts(5672)
            .withEnv("AMQ_USER", "quarkus")
            .withEnv("AMQ_PASSWORD", "quarkus");
//            .waitingFor(new HostPortWaitStrategy()
//                    .withStartupTimeout(Duration.ofMillis(5000))
//            );

    @Override
    public Map<String, String> start() {
        container.start();
        return Map.of(
                "quarkus.qpid-jms.url", "amqp://localhost:" + container.getFirstMappedPort(),
                "quarkus.qpid-jms.username", "quarkus",
                "quarkus.qpid-jms.password", "quarkus"
        );
    }

    @Override
    public void stop() {
        if (container.isRunning()) {
            container.stop();
        }
    }
}
