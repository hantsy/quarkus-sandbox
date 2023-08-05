package com.example;

import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.helpers.MultiEmitterProcessor;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.*;
import jakarta.json.bind.Jsonb;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Slf4j
public class MessageHandler {

    private static final Logger LOGGER = Logger.getLogger(MessageHandler.class.getName());

    @Inject
    JMSContext jmsContext;

    @Inject
    Queue helloQueue;

    @Inject
    Jsonb jsonb;

    public void send(String message) {
        var producer = jmsContext.createProducer();
        producer.setAsync(new CompletionListener() {
            @Override
            public void onCompletion(jakarta.jms.Message message) {
                try {
                    LOGGER.log(Level.INFO, "onCompletion: {0}",  message.getBody(String.class));
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onException(jakarta.jms.Message message, Exception exception) {
                LOGGER.log(Level.INFO, "onCompletion: {0}, exception: {1}",
                        new Object[]{message, exception}
                );
            }
        });
        producer.send(helloQueue, jsonb.toJson(new Message(message, Instant.now())));
    }

    MultiEmitterProcessor emitterProcessor = MultiEmitterProcessor.create();


    @Scheduled(delay = 500L, delayUnit = TimeUnit.MILLISECONDS, every = "1s")
    void receive() {
        var consumer = jmsContext.createConsumer(helloQueue);
        consumer.setMessageListener(
                msg -> {
                    try {
                        var received = jsonb.fromJson(msg.getBody(String.class), Message.class);
                        LOGGER.log(Level.INFO, "consuming message: {0}", received);
                        emitterProcessor.emit(received);
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
