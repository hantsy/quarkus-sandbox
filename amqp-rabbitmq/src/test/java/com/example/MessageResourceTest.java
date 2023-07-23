package com.example;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.SseEventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(RabbitMQTestResource.class)
class MessageResourceTest {
    private static final Logger LOGGER = Logger.getLogger(MessageResourceTest.class.getName());

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
        try (SseEventSource sse = SseEventSource.target(this.client.target(url.toExternalForm()))
                .reconnectingEvery(2, TimeUnit.SECONDS)
                .build()) {
            sse.register(
                    event -> {
                        var message = event.readData(Message.class);
                        assertThat(message.body()).isEqualTo("hello");
                    },
                    error -> LOGGER.log(Level.SEVERE, "error: {}", error),
                    () -> LOGGER.log(Level.INFO, "done")
            );
            sse.open();

            try (Response sendMessageResponse = this.client.target(url.toExternalForm())
                    .request()
                    .post(Entity.entity("hello", MediaType.TEXT_PLAIN_TYPE))) {
                assertThat(sendMessageResponse.getStatus()).isEqualTo(204);
            }
        }
    }
}