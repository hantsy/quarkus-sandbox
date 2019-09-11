package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@QuarkusTest
public class PostRepositoryTest {

    private final static Logger LOGGER = Logger.getLogger(AppInitializer.class.getName());

    @Inject
    private PostRepository posts;

    @Test
    @Transactional
    public void testPersistence() {
        LOGGER.info("The application is starting...");
        Post first = Post.of("Hello Quarkus", "My first post of Quarkus");
        Post second = Post.of("Hello Again, Quarkus", "My second post of Quarkus");

        this.posts.persist(first, second);
        this.posts.flush();
        this.posts.listAll().forEach(p -> System.out.println("Post:" + p));

    }


}