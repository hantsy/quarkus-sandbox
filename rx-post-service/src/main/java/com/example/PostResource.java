package com.example;

import io.reactivex.Observable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

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
    public Observable<Post> getAllPosts() {
        return this.posts.findAll();
    }


    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Observable<Post> getPostById(@PathParam("id") final String id) {
        return this.posts.getById(id);
    }

}
