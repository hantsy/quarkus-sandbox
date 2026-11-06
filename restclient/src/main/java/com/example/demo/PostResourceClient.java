package com.example.demo;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;

import java.util.List;

@Path("/posts")
@RegisterRestClient()
@RegisterProvider(PostResponseExceptionMapper.class)
public interface PostResourceClient {

    //see: https://github.com/quarkusio/quarkus/issues/34777
    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    Uni<Long> countAllPosts(@QueryParam("q") String q);

    // see: https://github.com/quarkusio/quarkus/issues/34771
    // can not use return type Multi<Post>
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

    @GET
    @Path("events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<PostCreated> events();
}
