package com.example

import com.example.repository.Post
import io.quarkus.runtime.StartupEvent
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.util.logging.Level
import java.util.logging.Logger


@ApplicationScoped
class DataInitializer(val postRepository: PostRepository) {
    companion object {
        private val LOGGER = Logger.getLogger(DataInitializer::class.java.name)
    }

    fun init(@Observes event: StartupEvent) {
        Multi.createFrom()
            .items(
                Post(title = "Quarkus 3.0", body = "test"),
                Post(title = "Quarkus 3.0 and Koltin", body = "test")
            )
            .flatMap{  postRepository.persist(it).convert().toPublisher() }
            .subscribe()
            .with(
                { data -> LOGGER.log(Level.INFO, "saved data: $data") },
                { error -> LOGGER.log(Level.ALL, "error: $error") }
            )
    }

    // see: https://github.com/quarkusio/quarkus/issues/34758S
    /*
    suspend fun init(@Observes event: StartupEvent) {
        //@formatter:off
        flowOf(
            Post(title = "Quarkus 3.0", body = "test"),
            Post(title = "Quarkus 3.0 and Koltin", body = "test")
        )
        .map {
            postRepository.persist(it).awaitSuspending()
        }
        .onEach {
            LOGGER.log(Level.INFO, "saved data: $it")
        }
        .onCompletion { LOGGER.log(Level.INFO, "initialization is done at: ${LocalDateTime.now()}") }
        .catch { LOGGER.log(Level.ALL, "error: $it") }
        .collect()
        //@formatter:on
    }
    */
}