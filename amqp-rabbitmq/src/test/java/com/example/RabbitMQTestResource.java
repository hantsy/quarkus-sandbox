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
        declareTopology();
//        Wait.defaultWaitStrategy()
//                .withStartupTimeout(Duration.ofMillis(30000))
//                .waitUntilReady(container);
        return Map.of(
                "amqp-host", container.getHost(),
                "amqp-port", container.getAmqpPort() + "",
                "amqp-username", container.getAdminUsername(),
                "amqp-password", container.getAdminPassword(),
                // this resource provides the broker, do not start a second one via DevServices
                "quarkus.amqp.devservices.enabled", "false"
        );
    }

    /**
     * RabbitMQ 4 refuses attaching an AMQP 1.0 sender link to an address that does not
     * resolve to an existing queue ({@code amqp:invalid-field / amqp_address_v1_not_permitted}),
     * and closes the client. Declare the queue before the application boots.
     */
    private void declareTopology() {
        IllegalStateException failure = null;
        for (int i = 0; i < 10; i++) {
            try {
                var result = container.execInContainer(
                        "rabbitmqadmin", "-u", container.getAdminUsername(), "-p", container.getAdminPassword(),
                        // rabbitmqadmin v2 (RabbitMQ 4) uses flag style arguments
                        "declare", "queue", "--name", "messages");
                if (result.getExitCode() == 0) {
                    return;
                }
                failure = new IllegalStateException(
                        "Unable to declare RabbitMQ topology: " + result.getStderr());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while declaring RabbitMQ topology", e);
            } catch (Exception e) {
                failure = new IllegalStateException("Unable to declare RabbitMQ topology", e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for RabbitMQ broker", e);
            }
        }
        throw failure;
    }

    @Override
    public void stop() {
        if (container.isRunning()) {
            container.stop();
        }
    }
}
