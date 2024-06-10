package com.example;

import com.example.domain.Post;
import com.example.repository.PostRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
//@TestProfile(H2Profile.class)
public class PostRepositoryTest {

    private final static Logger LOGGER = Logger.getLogger(PostRepositoryTest.class.getName());

    @Inject
    private PostRepository posts;

    @BeforeEach
    public void setup() {
        this.posts.deleteAll();
    }

    @Test
    public void testPersistence() {
        Post first = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").build();
        Post second = Post.builder().title("Hello Again, Quarkus").content("My second post of Quarkus").build();

        this.posts.insertAll(List.of(first, second));

        this.posts.findAll().forEach(p -> LOGGER.log(Level.INFO, "Post:{0}", p));
        assertEquals(2, this.posts.findAll().toList().size(), "result list size is 2");
    }

}