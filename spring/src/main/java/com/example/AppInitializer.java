package com.example;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.logging.Logger;

@ApplicationScoped
public class AppInitializer {
    private final static Logger LOGGER = Logger.getLogger(AppInitializer.class.getName());

    @Inject
    PostRepository posts;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        Post first = Post.of("Hello Quarkus", "My first post of Quarkus");
        Post second = Post.of("Hello Again, Quarkus", "My second post of Quarkus");

        this.posts.saveAll(Arrays.asList(first, second));
        this.posts.flush();

        this.posts.findAll().forEach(p -> System.out.println("Post:" + p));
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
