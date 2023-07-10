package org.example;


import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
//@QuarkusTestResource(H2DatabaseTestResource.class)
public class PostRepositoryTest {

    @Inject
    PostRepository postRepository;

    @Test
    public void testSave() {
        var assertSubscriber = postRepository.save(Post.of("test", "test"))
                .invoke(p -> assertThat(p.id).isNotNull())
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        assertSubscriber.awaitItem().assertCompleted();
    }
}
