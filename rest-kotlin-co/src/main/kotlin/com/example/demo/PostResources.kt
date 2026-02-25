package com.example.demo

import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.net.URI
import java.util.*
import java.util.logging.Logger

@ApplicationScoped
@Path("/posts")
class PostResources(private val posts: PostRepository) {
    companion object {
        private val LOGGER = Logger.getLogger(PostResources::class.java.name)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAllPosts(): Response {
        val data = posts.listAll().awaitSuspending()
        return Response.ok(data).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun savePost(formData: @Valid CreatePostCommand): Response {
        val post = Post(
            title = formData.title,
            content = formData.content
        )
        posts.persist(post).awaitSuspending()
        return Response.created(URI.create("/posts/" + post.id)).build()
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getPostById(@PathParam("id") id: UUID): Response {
        val post = posts.findById(id).awaitSuspending() ?: run {
            return Response.status(Response.Status.NOT_FOUND).build()
        }

        return Response.ok(post).build()

    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    suspend fun updatePost(@PathParam("id") id: UUID, updateForm: @Valid UpdatePostCommand): Response {
        val post = posts.findById(id).awaitSuspending() ?: run {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
        posts.update(post.apply {
            title = updateForm.title
            content = updateForm.content
        })

        return Response.noContent().build()
    }

    @DELETE
    @Path("{id}")
    suspend fun delete(@PathParam("id") id: UUID): Response {
        val deleted = posts!!.deleteById(id).awaitSuspending()
        return if (deleted) Response.noContent().build() else Response.status(Response.Status.NOT_FOUND).build()
    }
}
