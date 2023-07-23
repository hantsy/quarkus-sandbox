package com.example;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;

public class RabbitMQTestResource implements QuarkusTestResourceLifecycleManager {
    static RabbitMQContainer container = new RabbitMQContainer("rabbitmq:3-management-alpine")
            .withNetwork(Network.SHARED)
            .withNetworkAliases("rabbitmq")
            .withUser("quarkus","quarkus");
    @Override
    public Map<String, String> start() {
        container.start();
        Wait.forHealthcheck();
        return Map.of(
                "amqp-host", container.getHost(),
                "amqp-port",container.getFirstMappedPort()+"",
                "user-username",container.getAdminUsername(),
                "user-password", container.getAdminPassword()
        );
    }

    @Override
    public void stop() {
        if(container.isRunning()) {
            container.stop();
        }
    }
}
