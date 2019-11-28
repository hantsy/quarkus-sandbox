package com.example

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/posts")
@RegisterRestClient
interface PostResourceClient {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAllPosts(
            @QueryParam("q")
            q: String,
            @QueryParam("offset")
            @DefaultValue("0")
            offset: Int,
            @QueryParam("limit")
            @DefaultValue("10")
            limit: Int
    ): Response

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    fun countAllPosts(
            @QueryParam("q")
            q: String
    ): Response

}