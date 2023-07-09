package com.example

import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PostRepository : PanacheMongoRepository<Post> {
    fun findByTitle(title: String) = find("title", title).firstResult()
}