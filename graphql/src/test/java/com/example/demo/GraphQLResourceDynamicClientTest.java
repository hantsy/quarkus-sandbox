package com.example.demo;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the SmallRye GraphQL server through the dynamic client API. Injecting
 * {@code @Inject @GraphQLClient DynamicGraphQLClient} with an empty config key makes the client
 * resolve the "default" key, which in {@code @QuarkusTest} is auto-pointed at this application's own
 * {@code /graphql} endpoint, so no url has to be configured.
 */
@QuarkusTest
class GraphQLResourceDynamicClientTest {

    // An empty config key resolves to the producer's "default" client, which @QuarkusTest wires to
    // this application's own /graphql endpoint. GraphQLClient.value() has no default, hence "".
    @Inject
    @GraphQLClient("")
    DynamicGraphQLClient client;

    @Test
    void allPostsReturnsSeededData() throws Exception {
        Response response = client.executeSync("{ allPosts { id title content } }");

        assertFalse(response.hasError(), () -> "errors: " + response.getErrors());
        List<Post> posts = response.getList(Post.class, "allPosts");

        // DataInitializer seeds exactly four posts; createPost (other tests) only ever adds more.
        assertTrue(posts.size() >= 4, "expected at least the 4 seeded posts but got " + posts.size());
        List<String> titles = posts.stream().map(Post::title).toList();
        assertTrue(titles.containsAll(List.of("title #1", "title #2", "title #3", "title #4")),
                "seed titles missing from " + titles);
        assertTrue(posts.stream().allMatch(p -> p.id() != null && !p.id().isBlank()));
        assertTrue(posts.stream().filter(p -> p.content() != null)
                .allMatch(p -> p.content().startsWith("test content of #")));
    }

    @Test
    void postByIdWithArgumentReturnsTheMatchedPost() throws Exception {
        String id = client.executeSync("{ allPosts { id } }").getList(Post.class, "allPosts").get(0).id();

        Response response = client.executeSync(
                "query($postId: String!) { postById(postId: $postId) { id title content } }",
                Map.of("postId", id));

        assertFalse(response.hasError(), () -> "errors: " + response.getErrors());
        Post post = response.getObject(Post.class, "postById");

        assertNotNull(post);
        assertEquals(id, post.id());
        assertNotNull(post.title());
    }

    @Test
    void createPostMutationReturnsTheCreatedPost() throws Exception {
        Map<String, Object> input = Map.of("title", "Hello GraphQL", "content", "created through the dynamic client");

        Response response = client.executeSync(
                "mutation($input: CreatePostInput!) { createPost(createPostInput: $input) { id title content } }",
                Map.of("input", input));

        assertFalse(response.hasError(), () -> "errors: " + response.getErrors());
        Post created = response.getObject(Post.class, "createPost");

        assertNotNull(created.id());
        assertEquals("Hello GraphQL", created.title());
        assertEquals("created through the dynamic client", created.content());
    }
}
