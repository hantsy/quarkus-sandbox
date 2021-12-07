package com.example.entity

import io.quarkus.test.junit.QuarkusTest
import org.jboss.logging.Logger
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@QuarkusTest
class PersonTest {

    @Inject lateinit var log: Logger;

    @BeforeEach
    fun setUp() {
        val deletedCnt = Person.deleteAll()
        log.debug("deleted persons: $deletedCnt")

        val person = Person()
        person.name = "Stef"
        person.birth = LocalDate.of(1910, Month.FEBRUARY, 1)
        person.status = Status.Alive

        person.persist()

        Person.findAll().list().forEach {
            log.debug("saved person: $it")
        }

    }

    @Test
    fun `find by name`() {
        val found = Person.findByName("Stef")
        assertNotNull(found)
    }

    @Test
    fun `find alive persons`() {
        val alivePersons = Person.findAlive()
        assertEquals(1, alivePersons.size)
        val updated = Person.update("name = 'Mortal' where status = ?1", Status.Alive).all()
        assertEquals(1, updated)

        val alivePerson2 = Person.findAlive()
        assertEquals(1, alivePerson2.size)

        val found = Person.findByName("Stef")
        assertNull(found)
    }

}