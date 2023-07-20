package com.example.demo;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/posts")
@RegisterRestClient
@RegisterProvider(PostResponseExceptionMapper.class)
public interface PostResourceClient {

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    CompletionStage<Long> countAllPosts(@QueryParam("q") String q);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<List<com.example.demo.Post>> getAllPosts(
            @QueryParam("q") String q,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("10") int limit
    );

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<com.example.demo.Post> getPostById(@PathParam("id") String id);
}
