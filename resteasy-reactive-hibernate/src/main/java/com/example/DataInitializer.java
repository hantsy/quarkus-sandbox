package com.example;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Startup
@IfBuildProfile("dev")
public class DataInitializer {
    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PostRepository posts;

    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Post first = Post.of("Hello Quarkus", "My first post of Quarkus");
        Post second = Post.of("Hello Again, Quarkus", "My second post of Quarkus");

        this.posts.deleteAll()
                .onItem().invoke(l -> LOGGER.log(Level.INFO, "deleted {0} posts.", new Object[]{l}))
                .chain(d -> this.posts.save(first))
                .chain(d -> this.posts.save(second))
                .chain(v -> this.posts.findAll())
                .subscribe()
                .with(
                        rows -> rows.forEach(r -> LOGGER.log(Level.INFO, "data:{0}", r)),
                        err -> LOGGER.log(Level.SEVERE, "error:{0}", err.toString())
                );
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
