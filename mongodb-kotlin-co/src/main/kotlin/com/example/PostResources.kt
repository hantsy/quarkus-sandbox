package com.example

import com.example.repository.Post
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
import kotlinx.coroutines.jdk9.asFlow
import org.bson.types.ObjectId

@ApplicationScoped
@Path("/posts")
class PostResources(val postRepository: PostRepository) {

    @Inject
    lateinit var uriInfo: UriInfo

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun all() = postRepository.streamAll().asFlow()

    @POST
    suspend fun save(@Valid post: CreatePostCommand): Response {
        val data = Post(title = post.title, body = post.body)
        val saved = postRepository.persist(data).awaitSuspending()

        return Response
            .created(
                uriInfo.baseUriBuilder
                    .path("/posts/{id}")
                    .build(saved.id)
            )
            .build()
    }

    @GET
    @Path("{id}")
    suspend fun getById(@PathParam("id") id: ObjectId): Response = postRepository.findById(id)
        .awaitSuspending()?.let {
            Response.ok(it).build()
        } ?: Response.status(Response.Status.NOT_FOUND).build()

}