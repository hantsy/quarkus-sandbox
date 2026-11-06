package com.example.demo;

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
    public CompletionStage<Page> getAllPosts(
            @QueryParam("q") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        return this.client.getAllPosts(q, offset, limit)
                .thenCombine(
                        this.client.countAllPosts(q),
                        Page::new
                );
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Post> getPost(@PathParam("id") String id ){
        return this.client.getPostById(id);
    }

}
