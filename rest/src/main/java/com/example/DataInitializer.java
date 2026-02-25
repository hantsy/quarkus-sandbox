package com.example;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.VertxContextSupport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@IfBuildProfile("dev")
@RequiredArgsConstructor
public class DataInitializer {
    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    private final PostRepository posts;

    // There is an issue call reactive operations in the blocking codes.
    // see: https://github.com/quarkusio/quarkus/issues/14044
    @SneakyThrows
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Post first = Post.of("Hello Quarkus", "My first post of Quarkus");
        Post second = Post.of("Hello Again, Quarkus", "My second post of Quarkus");

        var result = VertxContextSupport.subscribeAndAwait(() ->
                Panache.withTransaction(() ->
                        posts.deleteAll()
                                .onItem().invoke(l -> LOGGER.log(Level.INFO, "deleted {0} posts.", l))
                                .chain(d -> posts.persist(first, second))
                                .chain(v -> posts.findAll().list())
                                .invoke(all -> LOGGER.log(Level.INFO, "found  {0} posts.", all.size()))
                )
        );

        LOGGER.log(Level.INFO, "The initialized result is: {0}", result);
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}