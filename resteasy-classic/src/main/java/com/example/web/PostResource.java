package com.example.web;

import com.example.domain.Post;
import com.example.repository.PostRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.Response.*;

@Path("/posts")
@RequestScoped
public class PostResource {
    private final static Logger LOGGER = Logger.getLogger(PostResource.class.getName());

    private final PostRepository posts;

    @Context
    ResourceContext resourceContext;

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
            @Parameter(name = "offset", in = ParameterIn.QUERY, description = "pagination offset")
            @QueryParam("offset") @DefaultValue("0") int offset,
            @Parameter(name = "limit", in = ParameterIn.QUERY, description = "pagination limit")
            @QueryParam("limit") @DefaultValue("10") int limit

    ) {
        return ok(this.posts.findByKeyword(q, offset, limit)).build();
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
            @PathParam("id") final String id) {
        return this.posts.findByIdOptional(id)
                .map(post -> ok(post).build())
                .orElseThrow(
                        () -> new PostNotFoundException(id)
                );
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("id") final String id, @Valid UpdatePostCommand post) {
        return this.posts.findByIdOptional(id)
                .map(existed -> {
                    existed.setTitle(post.title());
                    existed.setContent(post.content());

                    Post saved = this.posts.save(existed);
                    return noContent().build();
                })
                .orElseThrow(
                        () -> new PostNotFoundException(id)
                );
    }


    @Path("{id}")
    @DELETE
    public Response deletePost(@PathParam("id") final String id) {
        this.posts.deleteById(id);
        return noContent().build();
    }

    @Inject
    CommentResource commentResource;

    @Path("{id}/comments")
    public CommentResource postResource() {
        //return resourceContext.getResource(CommentResource.class);
        return resourceContext.initResource(commentResource);
    }
}
