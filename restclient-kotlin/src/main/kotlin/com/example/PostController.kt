package com.example

import org.eclipse.microprofile.rest.client.inject.RestClient
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.ok

@Path("/api")
@RequestScoped
class PostController {

    // see: https://stackoverflow.com/questions/59086151/rest-client-interface-can-not-be-injected-in-quarkus-kotlin-app
    // and https://github.com/quarkusio/quarkus/issues/5413
    @Inject
    @field:RestClient
    lateinit var client: PostResourceClient

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getPosts(@QueryParam("q")
                 q: String?,
                 @QueryParam("offset")
                 @DefaultValue("0")
                 offset: Int,
                 @QueryParam("limit")
                 @DefaultValue("10")
                 limit: Int): Response {
        val posts = this.client.getAllPosts(q, offset, limit).readEntity(List::class.java)
        val count = this.client.countAllPosts(q).readEntity(Long::class.java)
        println("posts: $posts \n count : $count")
        return ok(PostPage(posts as List<Post>, count)).build()
    }

}