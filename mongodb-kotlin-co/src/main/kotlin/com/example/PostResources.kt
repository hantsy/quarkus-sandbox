package com.example

import com.example.repository.Post
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriInfo
import org.bson.types.ObjectId

@ApplicationScoped
@Path("/posts")
class PostResources(val postRepository: PostRepository) {

    @Inject
    lateinit var uriInfo: UriInfo

    @GET
    suspend fun all() = postRepository.streamAll().asFlow()

    @POST
    suspend fun save(@Valid body: Post): Response {
        val saved = postRepository.persist(body).awaitSuspending()

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