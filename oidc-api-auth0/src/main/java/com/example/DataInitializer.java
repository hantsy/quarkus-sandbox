package com.example;

import com.example.domain.Post;
import com.example.repository.PostRepository;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PostRepository posts;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        Post first = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").build();
        Post second = Post.builder().title("Hello Again, Quarkus").content("My second post of Quarkus").build();

        this.posts.persist(first, second);
        this.posts.flush();

        LOGGER.info("Data initialization is done...");
        this.posts.listAll().forEach(p -> LOGGER.log(Level.INFO, "Saved Post: {0}", p));
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
