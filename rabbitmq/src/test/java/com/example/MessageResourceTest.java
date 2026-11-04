package com.example;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.smallrye.reactive.messaging.memory.TestingConnector;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.SseEventSource;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
@TestProfile(InMemoryProfile.class)
class MessageResourceTest {
    private static final Logger LOGGER = Logger.getLogger(MessageResourceTest.class.getName());

    @Inject
    @Connector("smallrye-testing")
    TestingConnector connector;

    @TestHTTPEndpoint(MessageResource.class)
    @TestHTTPResource
    URL url;

    Client client;

    @BeforeEach
    public void setup() {
        this.client = ClientBuilder.newClient();
    }

    @Test
    void testSendAndReceiveMessages() {
        LOGGER.log(Level.INFO, "base url: {0}", url);

        var messageReplay = new ArrayList<Message>();
        try (SseEventSource sse = SseEventSource.target(this.client.target(url.toExternalForm())).build()) {
            sse.register(event -> {
                LOGGER.log(Level.INFO, "handling event: {0}", event);
                var message = event.readData(Message.class, MediaType.APPLICATION_JSON_TYPE);
                LOGGER.log(Level.INFO, "reading event data: {0}", message);
                messageReplay.add(message);
            });
            sse.open();

            try (Response sendMessageResponse = this.client.target(url.toExternalForm())
                    .request()
                    .post(Entity.entity("hello", MediaType.TEXT_PLAIN_TYPE))) {
                assertThat(sendMessageResponse.getStatus()).isEqualTo(204);
            }

            // The outgoing `send` channel is switched to the testing connector,
            // the message emitted by the REST resource is captured there.
            await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                    assertThat(connector.sink("send").sent().getFirst().getPayload()).isEqualTo("hello"));

            // The original broker connector is not invoked at all, simulate the broker
            // delivering the message back through the incoming `messages` channel.
            connector.source("messages").deliver("hello");

            // The processed message should be broadcast to the SSE subscriber.
            await().atMost(Duration.ofSeconds(5)).untilAsserted(() ->
                    assertThat(messageReplay).hasSize(1));
        }

        assertThat(messageReplay.getFirst().body()).isEqualTo("hello");
    }
}
