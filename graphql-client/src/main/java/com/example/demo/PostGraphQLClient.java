package com.example.demo;

import io.smallrye.graphql.client.typesafe.api.ErrorOr;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

@GraphQLClientApi(configKey = "post-client-typesafe")
public interface PostGraphQLClient {
    @Query()
    List<Post> getAllPosts();

    @Query("allPosts")
    List<PostSummary> getAllPostSummaries();

    int countOfComments(@Source Post post);

    @Query
    @Description("Get a specific post by providing an id")
    ErrorOr<Post> getPostById(@Name("postId") String id);

    @Mutation
    @Description("Create a new post")
    Post createPost(CreatePost createPostInput);
}
