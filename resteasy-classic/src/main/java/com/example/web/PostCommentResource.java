package com.example.web;

import com.example.domain.Comment;
import com.example.domain.PostId;
import com.example.repository.CommentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.Response.created;
import static jakarta.ws.rs.core.Response.ok;


// see: https://stackoverflow.com/questions/57820428/jax-rs-subresource-issue-in-quarkus
// and Quarkus issue#3919: https://github.com/quarkusio/quarkus/issues/3919
//@Unremovable
//@RegisterForReflection
@ApplicationScoped
@RequiredArgsConstructor
public class PostCommentResource {
    private final static Logger LOGGER = Logger.getLogger(PostCommentResource.class.getName());
    private final CommentRepository comments;

    @Inject
    UriInfo uriInfo;

    @Inject
    ResourceContext resourceContext;

    @PathParam("id")
    UUID postId;

    @GET
    public Response getAllComments() {
        LOGGER.log(Level.INFO, "get comments of post id: {0}", postId);
        List<Comment> comments = this.comments.allByPostId(this.postId);
        LOGGER.log(Level.INFO, "comments data:{0}", comments);
        return ok(comments).build();
    }

    @POST
    public Response saveComment(@Valid CreateCommentCommand commentForm) {
        Comment comment = Comment.builder().post(new PostId(this.postId)).content(commentForm.content()).build();
        this.comments.persist(comment);
        return created(
                uriInfo.getBaseUriBuilder()
                        .path("/comments/{commentId}")
                        .build(this.postId, comment.getId())
        ).build();
    }
}
