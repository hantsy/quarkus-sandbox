package com.example.entity

import com.example.repository.Post
import com.example.PostRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
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
    fun `test save and query`() = runTest {
        val entity = Post(title = "foo", body = "bar")
        val saved = postRepository.persist(entity).awaitSuspending()
        saved.id shouldNotBe null

        val post = postRepository.findByTitle("foo").awaitSuspending()
        log.log(Level.INFO, "found post: $post")

        post shouldNotBe null
        post!!.title shouldBe "foo"
    }
}