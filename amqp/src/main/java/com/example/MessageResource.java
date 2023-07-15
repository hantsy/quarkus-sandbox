package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

@Path("/messages")
@Slf4j
public class MessageResource {

    @Inject
    MessageHandler handler;

    @Inject
    @Channel("data-stream")
    Publisher<Message> stream;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void send(String message) {
        log.info("sending: {}", message);
        handler.send(message);
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<Message> stream() {
        return stream;
    }
}
