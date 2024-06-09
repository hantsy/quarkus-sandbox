package com.example;

import com.example.repository.PostRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestProfile(H2Profile.class)
public class PostRepositoryTest {

    private final static Logger LOGGER = Logger.getLogger(PostRepositoryTest.class.getName());

    @Inject
    private PostRepository posts;

    @Test
    public void testPersistence() {
        this.posts.findAll().forEach(p -> LOGGER.log(Level.INFO, "Post:{0}", p));
        assertEquals(2, this.posts.findAll().toList().size(), "result list size is 2");
    }

}