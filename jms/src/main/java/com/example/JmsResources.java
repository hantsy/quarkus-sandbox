package com.example;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import org.jboss.logging.Logger;

@ApplicationScoped
public class JmsResources {
    private static final Logger LOGGER = Logger.getLogger(JmsResources.class.getName());
    private final static String HELLO_QUEUE = "HELLO_QUEUE";

    @Produces
    JMSContext jmsContext(ConnectionFactory connectionFactory) {
        return connectionFactory.createContext();
    }

    void closeJmsContext(@Disposes JMSContext jmsContext) {
        LOGGER.log(Logger.Level.INFO, "disposing JMSContext...");
        jmsContext.close();
    }

    @Produces
    Queue helloQueue(JMSContext jmsContext) {
        return jmsContext.createQueue(HELLO_QUEUE);
    }
}
