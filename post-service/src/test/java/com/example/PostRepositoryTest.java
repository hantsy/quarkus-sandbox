package com.example;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.logging.Logger;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestProfile(H2Profile.class)
public class PostRepositoryTest {

    private final static Logger LOGGER = Logger.getLogger(PostRepositoryTest.class.getName());

    @Inject
    private PostRepository posts;

    @Test
    public void testPersistence() {
        this.posts.listAll().forEach(p -> System.out.println("Post:" + p));
        Assertions.assertTrue(this.posts.findAll().list().size() == 2, "result list size is 2");
    }

}