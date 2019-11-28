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
class PostController(@Inject @RestClient val client: PostResourceClient) {

//    @Inject
//    @RestClient
//    lateinit var client: PostServiceClient

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getPosts(@QueryParam("q")
                 q: String,
                 @QueryParam("offset")
                 @DefaultValue("0")
                 offset: Int,
                 @QueryParam("limit")
                 @DefaultValue("10")
                 limit: Int): Response {
        val posts = this.client.getAllPosts(q, offset, limit).entity as List<Post>
        val count = this.client.countAllPosts(q).entity as Long
        return ok(PostPage(posts, count)).build()
    }

}