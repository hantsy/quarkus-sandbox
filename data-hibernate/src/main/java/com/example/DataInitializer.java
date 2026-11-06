package com.example;

import com.example.domain.Comment;
import com.example.domain.Post;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PostRepository posts;

    @Inject
    CommentRepository comments;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        Post first = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").build();
        Post second = Post.builder().title("Hello Again, Quarkus").content("My second post of Quarkus").build();

        this.posts.insertAll(List.of(first, second));
        this.posts.findAll()
                .forEach(p -> {
                    LOGGER.log(Level.INFO, "Post: {0}", new Object[]{p});
                    var comment = Comment.builder()
                            .content("Test Comment at " + LocalDateTime.now())
                            .post(p)
                            .build();
                    this.comments.insert(comment);
                });

        this.posts.findAll().forEach(p -> LOGGER.log(Level.INFO, "Post: {0}", new Object[]{p}));
        this.comments.findAll().forEach(c -> LOGGER.log(Level.INFO, "Comment: {0}", new Object[]{c}));
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
