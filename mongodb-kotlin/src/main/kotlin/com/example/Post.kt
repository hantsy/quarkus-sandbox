package com.example

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime


@MongoEntity
data class Post(
    @BsonId
    var id: ObjectId? = null,// used by MongoDB for the _id field var
    var title: String? = null,
    var body: String? = null,
    var createdAt: LocalDateTime? = LocalDateTime.now()
) {
   // constructor() : this(null, null, null, null)
}
