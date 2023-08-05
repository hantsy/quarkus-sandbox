package com.example;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@ApplicationScoped
public class JmsResources {
    private final static String HELLO_QUEUE = "HELLO_QUEUE";

    @Produces
    JMSContext jmsContext(ConnectionFactory connectionFactory) {
        return connectionFactory.createContext();
    }

    @Produces
    Queue helloQueue(JMSContext jmsContext) {
        return jmsContext.createQueue(HELLO_QUEUE);
    }
}
