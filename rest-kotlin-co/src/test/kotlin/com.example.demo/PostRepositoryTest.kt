package com.example.demo

import io.kotest.matchers.shouldNotBe
import io.quarkus.hibernate.reactive.panache.Panache
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.vertx.RunOnVertxContext
import io.quarkus.test.vertx.UniAsserter
import io.quarkus.test.vertx.UniAsserterInterceptor
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.util.function.Supplier

@QuarkusTest
class PostRepositoryTest {

    @Inject
    lateinit var postRepository: PostRepository

    class TransactionalUniAsserterInterceptor(asserter: UniAsserter) : UniAsserterInterceptor(asserter) {
        override fun <T> transformUni(uniSupplier: Supplier<Uni<T>>): Supplier<Uni<T>> {
            // Assert/execute methods are invoked within a database transaction
            return Supplier { Panache.withTransaction(uniSupplier) }
        }

        fun <T : Any> coAssertThat(suspendSupplier: suspend () -> T, asserter: (T) -> Unit): UniAsserter {
            return this.assertThat({
                Uni.createFrom().item {
                    runBlocking {
                        suspendSupplier()
                    }
                }
            }, asserter)
        }

    }

    @Test
    @RunOnVertxContext // required to inject UniAsserter
    fun testSave(asserter: UniAsserter) = runTest {
        val transactionalAsserter = TransactionalUniAsserterInterceptor(asserter)
        transactionalAsserter.coAssertThat(
            {
                val entity = Post(title = "foo", content = "bar")
                postRepository.persist(entity).awaitSuspending()
            },
            { entity -> entity.id shouldNotBe null }
        )
    }
}

