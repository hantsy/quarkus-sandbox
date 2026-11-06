package com.example

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@Path("/posts")
@RegisterRestClient
interface PostResourceClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllPosts(
        @QueryParam("q")
        q: String?,
        @QueryParam("offset")
        @DefaultValue("0")
        offset: Int = 0,
        @QueryParam("limit")
        @DefaultValue("10")
        limit: Int = 10
    ): Response

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    fun countAllPosts(
        @QueryParam("q")
        q: String?
    ): Response

}