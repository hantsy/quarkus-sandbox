package com.example;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/messages")
@Slf4j
public class MessageResource {

    @Inject
    @Channel("send")
    Emitter<String> emitter;

    @Inject
    @Channel("data-stream")
    Publisher<Message> stream;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void send(String message) {
        log.info("sending: {}", message);
        emitter.send(message);
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<Message> stream() {
        return stream;
    }
}
