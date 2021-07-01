package com.example;

import io.reactivex.*;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.*;

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
    @Path("test/maybe")
    @Produces(MediaType.APPLICATION_JSON)
    public Maybe<Integer> testMaybe() {
        return Maybe.just(1);
    }

    @GET
    @Path("test/completable")
    @Produces(MediaType.APPLICATION_JSON)
    public Completable testCompletable() {
        return Completable.fromObservable(Observable.just(1, 2, 3));
    }

    @GET
    @Path("test/flowable")
    @Produces(MediaType.APPLICATION_JSON)
    public Flowable<Integer> testFlowable() {
        return Flowable.just(1, 2, 3);
    }

    @GET
    @Path("test/single")
    @Produces(MediaType.APPLICATION_JSON)
    public Single<Integer> testSingle() {
        return Single.just(1);
    }

    @GET
    @Path("test/observable")
    @Produces(MediaType.APPLICATION_JSON)
    public Observable<Integer> testObservable() {
        return Observable.just(1, 2, 3);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Flowable<Post> getAllPosts() {
        return this.posts.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Single<Response> savePost(@Valid Post post) {
        return this.posts.save(post)
                .map(id -> created(URI.create("/posts/" + id)).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Single<Response> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(UUID.fromString(id))
                .map(data-> ok(data).build())
                .switchIfEmpty(Single.just(status(Status.NOT_FOUND).build()));
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Single<Response> updatePost(@PathParam("id") final String id, @Valid Post post) {
        return this.posts.update(UUID.fromString(id), post)
                .map(updated -> updated > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }

    @DELETE
    @Path("{id}")
    public Single<Response> delete(@PathParam("id") String id) {
        return this.posts.delete(UUID.fromString(id))
                .map(deleted -> deleted > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }
}
