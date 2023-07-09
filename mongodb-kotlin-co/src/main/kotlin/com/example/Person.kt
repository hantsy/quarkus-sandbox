package com.example

import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoEntity
import java.time.LocalDate

data class Person(
    var name: String? = null,
    var birth: LocalDate? = null,
    var status: Status? = null,
) : ReactivePanacheMongoEntity() {
    // constructor() : this(null, null, null)

    companion object : ReactivePanacheMongoCompanion<Person> {
        fun findByName(name: String) = find("name", name).firstResult()
        fun findAlive() = list("status", Status.Alive)
        fun deleteStefs() = delete("name", "Stef")
    }

    override fun toString(): String {
        return "Person(name='$name', birth=$birth, status=$status)"
    }

}

enum class Status {
    Alive, Dead
}