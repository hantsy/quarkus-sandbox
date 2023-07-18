package com.example.demo

import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.ok
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.eclipse.microprofile.rest.client.inject.RestClient

@Path("/api")
@RequestScoped
class PostController() {

    // see: https://stackoverflow.com/questions/59086151/rest-client-interface-can-not-be-injected-in-quarkus-kotlin-app
    // and https://github.com/quarkusio/quarkus/issues/5413
    @Inject
    @field:RestClient
    lateinit var client: PostResourceClient

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getPosts(
        @QueryParam("q")
        q: String?,
        @QueryParam("offset")
        @DefaultValue("0")
        offset: Int,
        @QueryParam("limit")
        @DefaultValue("10")
        limit: Int
    ): Response = coroutineScope {
        val posts = async { client.getAllPosts(q, offset, limit) }
        val count = async { client.countAllPosts(q) }
        ok(Page(posts.await(), count.await())).build()
    }

}