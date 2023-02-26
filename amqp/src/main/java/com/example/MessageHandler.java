package com.example;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
@Slf4j
public class MessageHandler {

    @Incoming("messages")
    @Outgoing("data-stream")
    @Broadcast
    Message receive(String message) {
        log.info("received: {}", message);
        return Message.builder().body(message).sentAt(Instant.now()).build();
    }
}
