package com.example;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import java.util.concurrent.Flow;

@Path("/messages")
@Slf4j
public class MessageResource {

    @Inject
    MessageHandler handler;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void send(String message) {
        log.info("sending: {}", message);
        handler.send(message);
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Multi<Message> stream() {
        return handler.emitterProcessor.toMulti();
        // see:
        // return FlowAdapters.toPublisher( handler.emitterProcessor.toMulti());
    }
}
