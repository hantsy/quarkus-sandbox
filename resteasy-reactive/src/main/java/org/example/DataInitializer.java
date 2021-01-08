package org.example;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PostRepository posts;

    // There is an issue call reactive opertions in the blocking codes.
    // see: https://github.com/quarkusio/quarkus/issues/14044
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Post first = Post.of("Hello Quarkus", "My first post of Quarkus");
        Post second = Post.of("Hello Again, Quarkus", "My second post of Quarkus");

        this.posts.deleteAll()
                .onItem().invoke(l -> LOGGER.log(Level.INFO, "deleted {0} posts.", new Object[]{l}))
                .flatMap(d -> this.posts.persist(List.of(first, second)))
                .flatMap(v -> this.posts.findAll().list())
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
