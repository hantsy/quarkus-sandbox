package com.example.web;

import com.example.domain.Post;
import com.example.repository.PostRepository;
import io.quarkus.security.Authenticated;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Path("/posts")
@RequestScoped
public class PostResource {
    private final static Logger LOGGER = Logger.getLogger(PostResource.class.getName());

    private final PostRepository posts;

    @Context
    UriInfo uriInfo;

    @Inject
    public PostResource(PostRepository posts) {
        this.posts = posts;
    }

    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response countAllPosts(@QueryParam("q") String q) {
        return ok(this.posts.countByKeyword(q)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts(
            @QueryParam("q") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit

    ) {
        return ok(this.posts.findByKeyword(q, offset, limit)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response savePost(@Valid CreatePostCommand post) {
        Post saved = this.posts.save(Post.builder().title(post.title()).content(post.content()).build());
        return created(
                uriInfo.getBaseUriBuilder()
                        .path("/posts/{id}")
                        .build(saved.getId())
        ).build();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostById(@PathParam("id") final String id) {
        return this.posts.findByIdOptional(id)
                .map(post -> ok(post).build())
                .orElse(status(NOT_FOUND).build());
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response updatePost(@PathParam("id") @NotEmpty final String id, @Valid UpdatePostCommand post) {
        return this.posts.findByIdOptional(id)
                .map(existed -> {
                    existed.setTitle(post.title());
                    existed.setContent(post.content());

                    Post saved = this.posts.save(existed);
                    return noContent().build();
                })
                .orElse(status(NOT_FOUND).build());
    }

    @Path("{id}/status")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response updatePostStatus(@PathParam("id") @NotEmpty final String id, @Valid UpdatePostStatusCommand status) {
        return this.posts.findByIdOptional(id)
                .map(existed -> {
                    existed.setStatus(status.status());
                    Post saved = this.posts.save(existed);
                    return noContent().build();
                })
                .orElse(status(NOT_FOUND).build());
    }


    @Path("{id}")
    @DELETE
    @Authenticated
    public Response deletePost(@PathParam("id") final String id) {
        this.posts.deleteById(id);
        return noContent().build();
    }


}
