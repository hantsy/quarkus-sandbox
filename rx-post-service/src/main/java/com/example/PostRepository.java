package com.example;

import io.reactivex.Observable;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowStream;
import io.vertx.reactivex.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public Observable<Post> findAll() {
        return this.client
                .rxBegin()
                .flatMapObservable(
                        tx -> tx.rxPreparedQuery("SELECT FROM posts")
                                .flatMapObservable(rs -> Observable.fromIterable(rs))
                                .doAfterTerminate(tx::commit)

                )
                .doOnSubscribe(row -> LOGGER.info("row::" + row.toString()))
                .map(row -> (Post.of(row.getString("id"), row.getString("title"), row.getString("content"))));
    }

    public Observable<Post> getById(String id) {
        return this.client
                .rxBegin()
                .flatMapObservable(
                        tx -> tx.rxPreparedQuery("SELECT FROM posts WHERE id=$1", Tuple.of(id))
                                .flatMapObservable(rs -> Observable.fromIterable(rs))
                                .take(1)
                                .doAfterTerminate(tx::commit)

                )
                .map(row ->
                        (Post.of(row.getString("id"), row.getString("title"), row.getString("content")))
                );
    }


}
