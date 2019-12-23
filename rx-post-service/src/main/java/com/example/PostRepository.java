package com.example;

import io.reactivex.*;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public Flowable<Post> findAll() {
        return this.client
                .rxBegin()
                .flatMapObservable(
                        tx -> tx.rxPrepare("SELECT * FROM posts")
                                .flatMapObservable(
                                        preparedQuery -> preparedQuery.createStream(50, Tuple.tuple())
                                                .toObservable()
                                )
                                .doAfterTerminate(tx::rxCommit)

                )
                .map(row -> Post.of(row.getUUID("id").toString(), row.getString("title"), row.getString("content")))
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    public Single<Post> findById(String id) {
        return this.client
                .rxBegin()
                .flatMapObservable(
                        tx -> tx.rxPrepare("SELECT * FROM posts WHERE id=$1")
                                .flatMapObservable(
                                        preparedQuery -> preparedQuery.createStream(1, Tuple.of(UUID.fromString(id)))
                                                .toObservable()

                                )
                                .doAfterTerminate(tx::rxCommit)

                )
                .firstOrError()
                .map(row -> Post.of(row.getUUID("id").toString(), row.getString("title"), row.getString("content")));
    }


}
