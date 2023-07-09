package com.example.web;

import com.example.domain.Comment;
import com.example.domain.PostId;
import com.example.repository.CommentRepository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.Response.created;
import static jakarta.ws.rs.core.Response.ok;


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
    public Response saveComment(@Valid CreateCommentCommand commentForm) {
        Comment saved = this.comments.save(Comment.builder().post(new PostId(this.postId)).content(commentForm.content()).build());
        return created(
                uriInfo.getBaseUriBuilder()
                        .path("/posts/{id}/comments/{commentId}")
                        .build(this.postId, saved.getId())
        ).build();
    }
}
