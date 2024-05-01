package com.example.demo;

import io.smallrye.graphql.client.typesafe.api.ErrorOr;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLClientApi(configKey = "post-client-typesafe")
public interface PostGraphQLClient {
    @Query()
    public List<Post> getAllPosts();

    @Query("allPosts")
    public List<PostSummary> getAllPostSummaries();

    public int countOfComments(@Source Post post);

    @Query
    @Description("Get a specific post by providing an id")
    public ErrorOr<Post> getPostById(@Name("postId") String id);

    @Mutation
    @Description("Create a new post")
    public Post createPost(CreatePost createPostInput);
}
