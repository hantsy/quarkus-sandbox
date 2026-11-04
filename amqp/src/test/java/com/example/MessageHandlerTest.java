package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.smallrye.reactive.messaging.memory.TestingConnector;
import io.smallrye.reactive.messaging.memory.TestOutgoing;
import io.smallrye.reactive.messaging.memory.TestIncoming;
import jakarta.enterprise.inject.Any;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
@TestProfile(InMemoryProfile.class)
class MessageHandlerTest {

    @Inject
    @Connector("smallrye-testing")
    TestingConnector connector;

    @Inject
    MessageHandler handler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void receive() {
        TestIncoming<String> messages = connector.incoming("messages");
        TestOutgoing<String> sink = connector.outgoing("send");
        TestOutgoing<Message> dataStream = connector.outgoing("messages-stream");
        
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