package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
@TestProfile(InMemoryProfile.class)
@Slf4j
class MessageHandlerTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @Inject
    MessageHandler handler;

    @Inject
    Config config;

    @BeforeEach
    void setUp() {
        final Iterable<String> propertyNames = config.getPropertyNames();
        log.debug("properties: {}", propertyNames);
//        for (final String propertyName : propertyNames) {
//            final String propertyValue = config.getValue(propertyName, String.class);
//            log.debug(propertyName + " = " + propertyValue);
//        }
    }

    @Test
    void receive() {
        InMemorySource<String> messages = connector.source("messages");
        InMemorySink<String> sink = connector.sink("send");
        InMemorySink<Message> dataStream = connector.sink("messages-stream");

        handler.send("hello");
        await().atMost(Duration.ofMillis(1000)).untilAsserted(() ->
                assertThat(sink.received().getFirst().getPayload()).isEqualTo("hello")
        );

        messages.send("hello-123");
        await().atMost(Duration.ofMillis(1000)).untilAsserted(() ->
                assertThat(dataStream.received().getFirst().getPayload().body()).isEqualTo("hello-123")
        );
    }
}