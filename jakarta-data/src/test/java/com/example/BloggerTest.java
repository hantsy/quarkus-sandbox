package com.example;

import com.example.domain.Post;
import com.example.domain.Status;
import com.example.service.Blogger;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.page.PageRequest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@Slf4j
public class BloggerTest {

    @Inject
    Blogger blogger;

    @BeforeEach
    public void init() {
        var deleted = blogger.deleteAllPosts();
        log.debug("deleted posts: {}", deleted);
        // blogger.byStatus(Status.DRAFT).forEach(blogger::delete);
    }

    @Test
    public void testPosts() {
        var blog = blogger.insert(Post.builder().title("Jakarta Data").content("content of Jakarta Data").build());
        // blogger.addComment(blog.getId(), Comment.builder().content("Test comment at " + LocalDateTime.now()).build());

        blogger.insert(Post.builder().title("Quarkus and Jakarta Data").content("content of Quarkus and Jakarta Data").build());

        var byTitlePattern = blogger.byTitle("%Jakarta%", PageRequest.ofPage(1, 10, true));
        assertThat(byTitlePattern.totalElements()).isEqualTo(2);
        byTitlePattern.content().forEach(post -> log.debug("byTitlePattern post: {}", post));

        var pagedPosts = blogger.allPosts("%Jakarta%", PageRequest.ofPage(1, 10, true));
        assertThat(pagedPosts.totalElements()).isEqualTo(2);
        pagedPosts.content().forEach(post -> log.debug("post: {}", post));
    }
}