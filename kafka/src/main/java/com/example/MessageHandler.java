package com.example;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Instant;

@ApplicationScoped
@Slf4j
public class MessageHandler {
    @Inject
    @Channel("send")
    Emitter<String> emitter;

    public void send(String message) {
        emitter.send(message);
    }

    @Incoming("messages")
    @Outgoing("messages-stream")
    @Broadcast
    Message receive(String message) {
        log.info("received: {}", message);
        return new Message(message, Instant.now());
    }
}
