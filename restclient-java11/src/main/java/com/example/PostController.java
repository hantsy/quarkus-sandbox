package com.example;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/api")
@RequestScoped
public class PostController {
    private static final Logger LOGGER= Logger.getLogger(PostController.class.getName());

    @Inject
    PostResourceClient client;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<PostPage> getAllPosts(
            @QueryParam("q") @DefaultValue("") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit
    ) {
        LOGGER.log(Level.INFO, String.format("get All Posts, q:%s, offset:%d, limit:%d", new Object[]{ q, offset, limit}));
        return this.client.getAllPosts(q, offset, limit)
                .thenCombine(
                        this.client.countAllPosts(q),
                        (data, count) -> PostPage.of(data, count)
                );
    }

}
