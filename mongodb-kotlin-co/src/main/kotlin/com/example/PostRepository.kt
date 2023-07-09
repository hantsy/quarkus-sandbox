package com.example

import com.example.repository.Post
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PostRepository : ReactivePanacheMongoRepository<Post> {
    fun findByTitle(title: String) = find("title", title).firstResult()
}