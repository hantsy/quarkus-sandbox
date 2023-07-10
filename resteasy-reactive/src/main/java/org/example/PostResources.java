package org.example;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.Response.*;

@Path("/posts")
@ApplicationScoped
public class PostResources {

    private final static Logger LOGGER = Logger.getLogger(PostResources.class.getName());

    private final PostRepository posts;

    @Inject
    public PostResources(PostRepository posts) {
        this.posts = posts;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllPosts() {
        return this.posts.listAll().map(data -> Response.ok(data).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> savePost(@Valid CreatePostRequest formData) {
        var post = Post.builder()
                .title(formData.title())
                .content(formData.content())
                .build();
        return this.posts.persist(post)
                .map(saved -> created(URI.create("/posts/" + saved.id)).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPostById(@PathParam("id") final UUID id) {
        return this.posts.findById(id)
                .map(data -> {
                    if (data == null) {
                        return null;
                    }
                    return ok(data).build();
                })
                .onItem().ifNull().continueWith(status(Status.NOT_FOUND).build());
        //.onFailure(PostNotFoundException.class).recoverWithItem(status(Status.NOT_FOUND).build());
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> updatePost(@PathParam("id") final UUID id, @Valid UpdatePostRequest updateForm) {
        return this.posts.findById(id)
                .onItem().ifNull().failWith(() -> new PostNotFoundException(id))
                .map(data -> Post.builder().id(id).title(updateForm.title()).content(updateForm.content()).build())
                .flatMap(this.posts::update)
                .map(updated -> updated > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build())
                .onFailure(PostNotFoundException.class).recoverWithItem(status(Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") UUID id) {
        return this.posts.deleteById(id)
                .map(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }
}
