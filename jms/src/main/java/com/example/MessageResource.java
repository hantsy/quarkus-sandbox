package com.example;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestStreamElementType;

@Path("/messages")
@Slf4j
public class MessageResource {

    @Inject
    MessageProducer sender;

    @Inject
    MessageConsumer consumer;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void send(String message) {
        log.info("sending: {}", message);
        sender.send(message);
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<Message> stream() {
        // see: https://github.com/quarkusio/quarkus/issues/35220
        return consumer.stream();
    }
}
