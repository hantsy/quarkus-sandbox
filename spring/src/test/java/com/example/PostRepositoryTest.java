package com.example;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class PostRepositoryTest {

    @Inject
    PostRepository postRepository;

    @Test
    public void testSave() {
        Post post = Post.of("test", "test content");
        var saved = postRepository.save(post);
        assertThat(saved).isNotNull();

        var postById = postRepository.findById(saved.id);
        assertThat(postById.isPresent()).isTrue();

        var data = postById.get();
        assertThat(data.title).isEqualTo("test");
    }
}
