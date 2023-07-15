package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

@QuarkusTest
class MessageHandlerTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @Inject
    MessageHandler handler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void receive() {
        MessageHandler handlerSpy = spy(handler);
        var inputCaptor = ArgumentCaptor.forClass(String.class);

        InMemorySource<String> messages = connector.source("messages");
        InMemorySink<String> sink = connector.sink("send");

        handler.send("hello");
        assertThat(sink.received().get(0).getPayload()).isEqualTo("hello");

        messages.send("hello");

        var outputResult = handlerSpy.receive(inputCaptor.capture());
        assertThat(inputCaptor.getValue()).isEqualTo("hello");
        assertThat(outputResult.body()).isEqualTo("hello");
    }
}