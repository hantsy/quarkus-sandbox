package com.example;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.hibernate.reactive.stage.Stage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    private Stage.Session session;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Post first = Post.of("Hello Quarkus", "My first post of Quarkus");
        Post second = Post.of("Hello Again, Quarkus", "My second post of Quarkus");

        this.session
                .withTransaction(
                        transaction -> this.session.createQuery("DELETE FROM Post")
                                .executeUpdate()
                                .thenCompose(r -> this.session.persist(first, second))
                )
                .thenCompose(r -> this.session.createQuery("SELECT * FROM Post", Post.class).getResultList())
                .thenApply(data -> {
                    data.forEach(row -> LOGGER.log(Level.INFO, "saved data:{0}", new Object[]{row}));
                    return null;
                })
                .toCompletableFuture()
                .join();

    }

    private String readInitScript() {
        try {
            return Files.readString(Paths.get(this.getClass().getResource("/init.sql").toURI()), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
