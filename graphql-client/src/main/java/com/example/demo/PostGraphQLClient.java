package com.example.demo;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import org.eclipse.microprofile.graphql.*;

import javax.validation.Valid;
import java.util.List;

@GraphQLClientApi
public interface PostGraphQLClient {

    @Query
    @Description("Get all posts")
    public List<Post> getAllPosts() ;

    public int countOfComments(@Source Post post);

    @Query
    @Description("Get a specific post by providing an id")
    public Post getPostById(@Name("postId") String id);

    @Mutation
    @Description("Create a new post")
    public Post createPost(@Valid CreatePost createPostInput);
}
