package com.example.demo;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/posts")
@RegisterRestClient()
public interface PostResourceClient {

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    Uni<Long> countAllPosts(@QueryParam("q") String q);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Uni<List<Post>> getAllPosts(
            @QueryParam("q") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit
    );

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Post> getPostById(@PathParam("id") String id);
}
