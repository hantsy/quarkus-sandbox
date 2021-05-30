package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.graphql.*;

import java.util.List;
import java.util.Optional;

@GraphQLApi
@RequiredArgsConstructor
public class GraphQLResource {
    final PostService postService;

    @Query
    @Description("Get all posts")
    public List<Post> getAllPosts() {
        return this.postService.getAllPosts();
    }

    @Query
    @Description("Get a specific post by providing an id")
    public Optional<Post> getPostById(@Name("postId") String id) {
        return this.postService.getPostById(id);
    }

    @Mutation
    @Description("Create a new post")
    public Post createPost(CreatePost createPostInput){
        return this.postService.createPost(createPostInput);
    }
}
