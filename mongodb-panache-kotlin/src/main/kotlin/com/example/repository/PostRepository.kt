package com.example.repository

import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository

class PostRepository: PanacheMongoRepository<Post> {
    fun findByTitle(title: String) = find("title", title).firstResult()
}