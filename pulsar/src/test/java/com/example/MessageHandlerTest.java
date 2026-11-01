package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.smallrye.reactive.messaging.memory.TestingConnector;
import io.smallrye.reactive.messaging.memory.TestOutgoing;
import io.smallrye.reactive.messaging.memory.TestIncoming;
import jakarta.enterprise.inject.Any;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
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
    @Connector("smallrye-testing")
    TestingConnector connector;

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
        TestIncoming<String> messages = connector.source("messages");
        TestOutgoing<String> sink = connector.sink("send");
        TestOutgoing<Message> dataStream = connector.sink("messages-stream");

        handler.send("hello");
        await().atMost(Duration.ofMillis(1000)).untilAsserted(() ->
                assertThat(sink.sent().getFirst().getPayload()).isEqualTo("hello")
        );

        messages.deliver("hello-123");
        await().atMost(Duration.ofMillis(1000)).untilAsserted(() ->
                assertThat(dataStream.sent().getFirst().getPayload().body()).isEqualTo("hello-123")
        );
    }
}