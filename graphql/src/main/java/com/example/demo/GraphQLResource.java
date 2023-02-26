package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.graphql.*;

import jakarta.validation.Valid;
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

    public int countOfComments(@Source Post post) {
        return post.comments.size();
    }

    @Query
    @Description("Get a specific post by providing an id")
    public Post getPostById(@Name("postId") String id) {
        // result:
        //      data: {
        //          postById : null
        //      }
        // return this.postService.getPostById(id);

        return this.postService.getPostById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    @Mutation
    @Description("Create a new post")
    public Post createPost(@Valid CreatePost createPostInput) {
        return this.postService.createPost(createPostInput);
    }
}
