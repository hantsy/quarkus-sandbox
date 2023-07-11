package com.example;


import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class PostRepositoryTest {

    @Inject
    PostRepository postRepository;

    static class TransactionalUniAsserterInterceptor extends UniAsserterInterceptor {

        public TransactionalUniAsserterInterceptor(UniAsserter asserter) {
            super(asserter);
        }

        @Override
        protected <T> Supplier<Uni<T>> transformUni(Supplier<Uni<T>> uniSupplier) {
            // Assert/execute methods are invoked within a database transaction
            return () -> Panache.withTransaction(uniSupplier);
        }
    }

    @Test
    @RunOnVertxContext // required to inject UniAsserter
    public void testSave(UniAsserter asserter) {
        var transactionalAsserter = new TransactionalUniAsserterInterceptor(asserter);
        transactionalAsserter.assertThat(() ->
                        postRepository.persist(Post.of("test", "test")),
                post -> assertThat(post.id).isNotNull()
        );
    }
}
