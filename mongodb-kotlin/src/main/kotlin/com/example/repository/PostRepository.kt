package com.example.repository

import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository
import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PostRepository : PanacheMongoRepository<Post> {
    fun findByTitle(title: String) = find("title", title).firstResult()
}