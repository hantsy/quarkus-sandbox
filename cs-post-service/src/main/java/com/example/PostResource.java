package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.concurrent.CompletionStage;
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

//    @Context
//    UriInfo uriInfo;

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
        return this.posts.save(post).thenApply(id -> created(URI.create("/posts/"+ id.toString())).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(id).thenApply(post -> post != null ? ok(post) : status(NOT_FOUND))
                .thenApply(ResponseBuilder::build);
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> delete(@PathParam("id") String id) {
        return this.posts.delete(id)
                .thenApply(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .thenApply(status -> Response.status(status).build());
    }

}
