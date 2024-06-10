package com.example;

import com.example.domain.Comment;
import com.example.domain.Post;
import com.example.domain.Status;
import com.example.service.Blogger;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Limit;
import jakarta.data.Order;
import jakarta.data.Sort;
import jakarta.data.page.PageRequest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    }

    @Test
    public void testPosts() {
        var blog = blogger.insert(Post.builder().title("Jakarta Data").content("content of Jakarta Data").build());
        blogger.insert(Comment.builder().post(blog).content("test comment").build());
//        var comments = blogger.getCommentsOfPost(blog.getId());
//        comments.forEach(c -> log.debug("inserted comment: {}",  c.toString()));
//        assertThat(comments.size()).isEqualTo(1);

        blogger.insert(Post.builder().title("Quarkus and Jakarta Data").content("content of Quarkus and Jakarta Data").build());

        var byTitlePattern = blogger.byTitle("%Jakarta%", PageRequest.ofPage(1, 10, true));
        assertThat(byTitlePattern.totalElements()).isEqualTo(2);
        byTitlePattern.content().forEach(post -> log.debug("byTitlePattern post: {}", post));

        var byStatusPattern = blogger.byStatus(Status.DRAFT, Order.by(List.of(Sort.desc("createdAt"), Sort.asc("title"))), Limit.range(1, 10));
        assertThat(byStatusPattern.size()).isEqualTo(2);
        byStatusPattern.forEach(post -> log.debug("byStatusPattern post: {}", post));

        var pagedPosts = blogger.allPosts("%Jakarta%", PageRequest.ofPage(1, 10, true));
        assertThat(pagedPosts.totalElements()).isEqualTo(2);
        pagedPosts.content().forEach(post -> log.debug("post: {}", post));
    }
}