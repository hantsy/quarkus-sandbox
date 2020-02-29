package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("/posts")
@RequestScoped
public class PostResource {
    private final static Logger LOGGER = Logger.getLogger(PostResource.class.getName());

    private final PostRepository posts;

    @Inject
    public PostResource(PostRepository posts) {
        this.posts = posts;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> getAllPosts() {
        return this.posts.findAll().thenApply(posts -> ok(posts).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> savePost(@Valid Post post/*, @Context UriInfo uriInfo*/) {
        //uriInfo.getBaseUriBuilder().path("/posts/{id}").build(id.toString())
        return this.posts.save(post).thenApply(id -> created(URI.create("/posts/" + id.toString())).build());
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> updatePost(@PathParam("id") final String id, @Valid Post post) {
        return this.posts.update(UUID.fromString(id), post)
                .thenApply(updated -> updated > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .thenApply(status -> status(status).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(UUID.fromString(id))
                .thenApply(post -> ok(post).build())
                .exceptionally(throwable -> {
                    LOGGER.log(Level.WARNING, " failed to get post by id :", throwable);
                    return status(NOT_FOUND).build();
                });
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> delete(@PathParam("id") String id) {
        return this.posts.delete(id)
                .thenApply(deleted -> deleted > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .thenApply(status -> status(status).build());
    }

}
