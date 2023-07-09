package com.example

import io.quarkus.mongodb.panache.kotlin.PanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntity
import java.time.LocalDate

data class Person(
    var name: String? = null,
    var birth: LocalDate? = null,
    var status: Status? = null,
) : PanacheMongoEntity() {
    // constructor() : this(null, null, null)

    companion object : PanacheMongoCompanion<Person> {
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