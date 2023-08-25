package com.example;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.json.bind.Jsonb;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Slf4j
public class MessageConsumer {

    private static final Logger LOGGER = Logger.getLogger(MessageConsumer.class.getName());

    @Inject
    JMSContext jmsContext;

    @Inject
    Queue helloQueue;

    @Inject
    Jsonb jsonb;

    UnicastProcessor<Message> processor = UnicastProcessor.create();

    Multi<Message> messageStream = processor.toHotStream();
    public void receive(@Observes StartupEvent startupEvent) {
        var consumer = jmsContext.createConsumer(helloQueue);
        consumer.setMessageListener(
                msg -> {
                    try {
                        var received = jsonb.fromJson(msg.getBody(String.class), Message.class);
                        LOGGER.log(Level.INFO, "consuming message: {0}", received);
                        processor.onNext(received);
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
//        var message = consumer.receiveBody(String.class, 500);
//        if (message != null) {
//            var received = jsonb.fromJson(message, Message.class);
//            LOGGER.log(Level.INFO, "received message: {0}", received);
//            emitterProcessor.emit(received);
//        }
    }
}
