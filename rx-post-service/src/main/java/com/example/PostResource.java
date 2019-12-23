package com.example;

import io.reactivex.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Single<Post> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(id);
    }

}
