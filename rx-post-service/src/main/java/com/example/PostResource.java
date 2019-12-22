package com.example;

import io.reactivex.Maybe;
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
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public Observable<Integer> testObservable() {
        return Observable.just(1, 2, 3);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Observable<Post> getAllPosts() {
        return this.posts.findAll();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Maybe<Post> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(id);
    }

}
