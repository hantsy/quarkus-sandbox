package com.example

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
    fun all() = postRepository.streamAll()

    @POST
    fun save(@Valid body: Post): Response {
        val saved = postRepository.persist(body)

        return Response
            .created(
                uriInfo.baseUriBuilder
                    .path("/posts/{id}")
                    .build(body.id)
            )
            .build()
    }

    @GET
    @Path("{id}")
    fun getById(@PathParam("id") id: ObjectId): Response = postRepository.findById(id)
        ?.let {
            Response.ok(it).build()
        } ?: Response.status(Response.Status.NOT_FOUND).build()

}