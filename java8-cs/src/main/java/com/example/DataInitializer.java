package com.example;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.axle.pgclient.PgPool;
import io.vertx.axle.sqlclient.Tuple;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    private PgPool client;

    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Tuple first = Tuple.of("Hello Quarkus", "My first post of Quarkus");
        Tuple second = Tuple.of("Hello Again, Quarkus", "My second post of Quarkus");

//        String initScript = readInitScript();
//        LOGGER.info("initialized script::" + initScript);
//        client.query(initScript)
//                .thenCompose(r -> client.query("DELETE FROM posts"))
         client.query("DELETE FROM posts")
                .thenCompose(r -> client.preparedBatch("INSERT INTO posts (title, content) VALUES ($1, $2)", List.of(first, second)))
                .thenCompose(r -> client.query("SELECT * FROM posts"))
                .toCompletableFuture()
                .join();

    }

    private String readInitScript() {
        try {
            return Files.readString(Paths.get(this.getClass().getResource("/init.sql").toURI()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
