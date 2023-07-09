package com.example

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.logging.Level
import java.util.logging.Logger

@QuarkusTest
class PostRepositoryTest {
    companion object {
        private val log = Logger.getLogger(PostRepositoryTest::class.java.name)
    }


    @Inject
    lateinit var postRepository: PostRepository

    @Test
    fun `test save and query`() {
        val entity = Post(title = "foo", body = "bar")
        postRepository.persist(entity)

        log.log(Level.INFO, "entity: $entity")
        assertThat(entity).isNotNull()

        val post = postRepository.findByTitle("foo")
        log.log(Level.INFO, "found post: $post")
        assertThat(post!!.title).isEqualTo("foo")

    }
}