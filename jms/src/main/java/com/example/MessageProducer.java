package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.CompletionListener;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.json.bind.Jsonb;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Slf4j
public class MessageProducer {

    private static final Logger LOGGER = Logger.getLogger(MessageProducer.class.getName());

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
                    LOGGER.log(Level.INFO, "onCompletion: {0}", message.getBody(String.class));
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onException(jakarta.jms.Message message, Exception exception) {
                LOGGER.log(Level.INFO, "exception: {0}",
                        new Object[]{exception}
                );
            }
        });
        producer.send(helloQueue, jsonb.toJson(new Message(message, Instant.now())));
    }
}
