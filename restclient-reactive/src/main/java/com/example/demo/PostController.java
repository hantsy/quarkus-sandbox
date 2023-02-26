package com.example.demo;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api")
@RequestScoped
public class PostController {
    @Inject
    @RestClient
    PostResourceClient client;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PostPage> getAllPosts(
            @QueryParam("q") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        return Uni.combine().all()
                .unis(
                        this.client.getAllPosts(q, offset, limit).collect().asList(),
                        this.client.countAllPosts(q)
                )
                .combinedWith(
                        results -> PostPage.of((List<Post>) results.get(0), (Long) results.get(1))
                );
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Post> getPost(@PathParam("id") String id) {
        return this.client.getPostById(id);
    }

}
