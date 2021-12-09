package com.example;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.*;
import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.*;

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
    public Multi<Post> getAllPosts() {
        return this.posts.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> savePost(@Valid Post post) {
        return this.posts.save(post)
                .map(id -> created(URI.create("/posts/" + id)).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(UUID.fromString(id))
                .map(data -> {
                    if (data == null) {
                        return null;
                    }
                    return ok(data).build();
                })
        //        .onItem().ifNull().continueWith(status(Status.NOT_FOUND).build());
        .onFailure(PostNotFoundException.class).recoverWithItem(status(Status.NOT_FOUND).build());
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> updatePost(@PathParam("id") final String id, @Valid Post post) {
        return this.posts.update(UUID.fromString(id), post)
                .map(updated -> updated > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") String id) {
        return this.posts.delete(UUID.fromString(id))
                .map(deleted -> deleted > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }
}
