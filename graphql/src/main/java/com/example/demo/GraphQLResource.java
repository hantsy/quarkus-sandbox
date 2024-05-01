package com.example.demo;

import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.graphql.*;

import java.util.List;

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
        if (post.comments() != null) {
            return post.comments().size();
        }

        return 0;
    }

    @Query
    @Description("Get a specific post by providing an id")
    public Post getPostById(@Name("postId") String id) {
        // result:
        //      data: {
        //          postById : null
        //      }
        // return this.postService.getPostById(id);

        return this.postService.getPostById(id);
    }

    @Mutation
    @Description("Create a new post")
    public Post createPost(@Valid CreatePost createPostInput) {
        return this.postService.createPost(createPostInput);
    }

    @Subscription
    @Description("Notify when post was created")
    public Multi<PostCreated> postCreated(){
        return this.postService.postCreated();
    }
}
