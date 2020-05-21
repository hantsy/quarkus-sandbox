package com.example;

import io.smallrye.mutiny.Uni;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/api")
@RequestScoped
public class PostController {

    @Inject
    PostResourceClient client;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<PostPage> getAllPosts(
            @QueryParam("q") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        return Uni.combine().all().unis(
                this.client.getAllPosts(q, offset, limit).collectItems().asList(),
                this.client.countAllPosts(q)
                )
                .combinedWith(
                        results-> PostPage.of((List<Post>) results.get(0), (Long) results.get(1))
                );
    }

}
