package com.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Path("/api")
@RequestScoped
public class PostController {

    @Inject
    PostResourceClient client;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<PostPage> getAllPosts(
            @QueryParam("q") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        return this.client.getAllPosts(q, offset, limit)
                .thenCombine(
                        this.client.countAllPosts(q),
                        (data, count) -> PostPage.of(data, count)
                );
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getPost(@PathParam("id") String id ){
        return this.client.getPostById(id);
    }

}
