package com.example

import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month
import java.util.logging.Level
import java.util.logging.Logger

@OptIn(ExperimentalCoroutinesApi::class)
@QuarkusTest
class PersonTest {
    companion object {
        private val log = Logger.getLogger(PersonTest::class.java.name)
    }

    @BeforeEach
    fun setUp() = runTest {
        val deletedCnt = Person.deleteAll().awaitSuspending()
        log.log(Level.INFO, "deleted persons: $deletedCnt")

        val person = Person(
            name = "Stef",
            birth = LocalDate.of(1910, Month.FEBRUARY, 1),
            status = Status.Alive
        )

        person.persist<Person>().awaitSuspending()

        Person.streamAll()
            .asFlow()
            .onEach {
                log.log(Level.INFO, "saved person: $it")
            }
            .collect()

    }

    @Test
    fun `find by name`() = runTest {
        val found = Person.findByName("Stef")
        assertNotNull(found)
    }

    @Test
    fun `find alive persons`() = runTest {
        val alivePersons = Person.findAlive().awaitSuspending()
        assertEquals(1, alivePersons.size)
        val updated = Person.update("name = 'Mortal' where status = ?1", Status.Alive)
            .all()
            .awaitSuspending()
        assertEquals(1, updated)

        val alivePerson2 = Person.findAlive().awaitSuspending()
        assertEquals(1, alivePerson2.size)

        val found = Person.findByName("Stef").awaitSuspending()
        assertNull(found)
    }

}