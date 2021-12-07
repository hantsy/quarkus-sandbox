package com.example.entity

import io.quarkus.mongodb.panache.kotlin.PanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntity
import java.time.LocalDate

class Person : PanacheMongoEntity() {
    companion object : PanacheMongoCompanion<Person> {
        fun findByName(name: String) = find("name", name).firstResult()
        fun findAlive() = list("status", Status.Alive)
        fun deleteStefs() = delete("name", "Stef")
    }

    lateinit var name: String
    lateinit var birth: LocalDate
    lateinit var status: Status

    override fun toString(): String {
        return "Person(name='$name', birth=$birth, status=$status)"
    }

}

enum class Status {
    Alive, Dead
}