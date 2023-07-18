package com.example.demo

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@Path("/posts")
@RegisterRestClient
interface PostResourceClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllPosts(
        @QueryParam("q")
        q: String?,
        @QueryParam("offset")
        @DefaultValue("0")
        offset: Int = 0,
        @QueryParam("limit")
        @DefaultValue("10")
        limit: Int = 10
    ): List<Post>

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun countAllPosts(
        @QueryParam("q")
        q: String?
    ): Long

}