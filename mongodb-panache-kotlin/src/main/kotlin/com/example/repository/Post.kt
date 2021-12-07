package com.example.repository

import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Post(
    var id: ObjectId? = null,// used by MongoDB for the _id field var
    var title: String,
    var body: String,
    var createdAt: LocalDateTime? = LocalDateTime.now()
)
