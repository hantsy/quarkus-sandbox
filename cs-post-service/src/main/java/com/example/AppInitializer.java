package com.example;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.axle.pgclient.PgPool;
import io.vertx.axle.sqlclient.Tuple;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class AppInitializer {
    private final static Logger LOGGER = Logger.getLogger(AppInitializer.class.getName());

    @Inject
    private PgPool client;

    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        LOGGER.info("pg client::" + client);

        Tuple first = Tuple.of("Hello Quarkus", "My first post of Quarkus");
        Tuple second = Tuple.of("Hello Again, Quarkus", "My second post of Quarkus");

        client.query("CREATE EXTENSION IF NOT EXISTS pgcrypto;\n" +
                "CREATE TABLE IF NOT EXISTS posts(\n" +
                "id UUID PRIMARY KEY DEFAULT gen_random_uuid() ,\n" +
                "title VARCHAR(255) NOT NULL,\n" +
                "content VARCHAR(255) NOT NULL\n" +
                ");")
                .thenCompose(r-> client.query("DELETE FROM posts"))
                .thenCompose(r -> client.preparedBatch("INSERT INTO posts (title, content) VALUES ($1, $2)", List.of(first, second)))
                .thenCompose(r -> client.query("SELECT * FROM posts"))
                .toCompletableFuture()
                .join();

    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
