package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.ok;


// see: https://stackoverflow.com/questions/57820428/jax-rs-subresource-issue-in-quarkus
// and Quarkus issue#3919: https://github.com/quarkusio/quarkus/issues/3919
//@Unremovable
//@RegisterForReflection
@RequestScoped
public class CommentResource {
    private final static Logger LOGGER = Logger.getLogger(CommentResource.class.getName());
    private final CommentRepository comments;

    @Context
    UriInfo uriInfo;

    @Context
    ResourceContext resourceContext;

    @PathParam("id")
    String postId;

    @Inject
    public CommentResource(CommentRepository commentRepository) {
        this.comments = commentRepository;
    }

    @GET
    public Response getAllComments() {
        return ok(this.comments.allByPostId(this.postId)).build();
    }

    @POST
    public Response saveComment(@Valid CommentForm commentForm) {
        Comment saved = this.comments.save(Comment.builder().post(new PostId(this.postId)).content(commentForm.getContent()).build());
        return created(
                uriInfo.getBaseUriBuilder()
                        .path("/posts/{id}/comments/{commentId}")
                        .build(this.postId, saved.getId())
        ).build();
    }
}
