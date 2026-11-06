package com.example.web;

import com.example.domain.Post;
import com.example.service.Blogger;
import jakarta.data.page.PageRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.Response.*;

@Path("/posts")
@ApplicationScoped
public class PostResources {
    private final static Logger LOGGER = Logger.getLogger(PostResources.class.getName());

    @Inject
    Blogger posts;

    @Inject
    ResourceContext resourceContext;

    //@Context will be removed in Jakarta Rest 4.0
    @Inject
    UriInfo uriInfo;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all Posts",
            description = "Get all posts"
    )
    @APIResponse(
            responseCode = "200",
            name = "Post list",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            type = SchemaType.ARRAY,
                            implementation = Post.class
                    )
            )
    )
    public Response getAllPosts(
            @Parameter(name = "q", in = ParameterIn.QUERY, description = "keyword match tile or content")
            @QueryParam("q") String q,
            @Parameter(name = "offset", in = ParameterIn.QUERY, description = "pagination pagenumber")
            @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(name = "limit", in = ParameterIn.QUERY, description = "pagination page size")
            @QueryParam("size") @DefaultValue("10") int size

    ) {
        return ok(this.posts.allPosts(q, PageRequest.ofPage(page, size, true))).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create a new post",
            description = "Create a new post, if it is successful return 201 status code"
    )
    @APIResponse(
            responseCode = "201",
            headers = {
                    @Header(name = "Location", description = "The URL of the new created post")
            }
    )
    public Response savePost(
            @RequestBody(
                    description = "request body of creating a post",
                    content = {
                            @Content(
                                    schema = @Schema(
                                            type = SchemaType.OBJECT,
                                            implementation = CreatePostCommand.class
                                    )
                            )
                    }
            )
            @Valid CreatePostCommand post) {
        var entity = Post.builder().title(post.title()).content(post.content()).build();
        this.posts.insert(entity);
        return created(
                uriInfo.getBaseUriBuilder()
                        .path("/posts/{id}")
                        .build(entity.getId())
        ).build();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get post by id",
            description = "Get post by id, if not found return a 404 status code."
    )
    @APIResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            type = SchemaType.OBJECT,
                            implementation = Post.class
                    )
            )
    )
    public Response getPostById(
            @Parameter(name = "id", in = ParameterIn.PATH, description = "post id")
            @PathParam("id") final UUID id) {
        return this.posts.byId(id)
                .map(post -> ok(post).build())
                .orElseThrow(
                        () -> new PostNotFoundException(id)
                );
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("id") final UUID id, @Valid UpdatePostCommand post) {
        return this.posts.byId(id)
                .map(existed -> {
                    existed.setTitle(post.title());
                    existed.setContent(post.content());

                    this.posts.update(existed);
                    return noContent().build();
                })
                .orElseThrow(
                        () -> new PostNotFoundException(id)
                );
    }


    @Path("{id}")
    @DELETE
    public Response deletePost(@PathParam("id") final UUID id) {
        return this.posts.byId(id)
                .map(existed -> {
                    this.posts.delete(existed);
                    return noContent().build();
                })
                .orElseThrow(
                        () -> new PostNotFoundException(id)
                );

    }

    ;

    @GET
    @Path("{id}/comments")
    public Response getAllComments(@PathParam("id") UUID postId) {
        LOGGER.log(Level.INFO, "get comments of post id: {0}", postId);
        var comments = this.posts.getCommentsOfPost(postId);
        return ok(comments).build();
    }

}
