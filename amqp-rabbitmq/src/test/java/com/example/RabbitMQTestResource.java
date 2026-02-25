package com.example;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.Network;
import org.testcontainers.rabbitmq.RabbitMQContainer;

import java.util.Map;

public class RabbitMQTestResource implements QuarkusTestResourceLifecycleManager {
    static RabbitMQContainer container = new RabbitMQContainer("rabbitmq:4-management-alpine")
            .withNetwork(Network.SHARED)
            .withNetworkAliases("rabbitmq")
            .withAdminUser("quarkus")
            .withAdminPassword("quarkus");

    @Override
    public Map<String, String> start() {
        container.start();
//        Wait.defaultWaitStrategy()
//                .withStartupTimeout(Duration.ofMillis(30000))
//                .waitUntilReady(container);
        return Map.of(
                "amqp-host", container.getHost(),
                "amqp-port", container.getAmqpPort() + "",
                "amqp-username", container.getAdminUsername(),
                "amqp-password", container.getAdminPassword()
        );
    }

    @Override
    public void stop() {
        if (container.isRunning()) {
            container.stop();
        }
    }
}
