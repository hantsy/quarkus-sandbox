package com.example;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
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
                .flatMapPublisher(
                        tx -> tx.rxPrepare("SELECT * FROM posts")
                                .flatMapPublisher(
                                        preparedQuery -> preparedQuery.createStream(50, Tuple.tuple())
                                                .toFlowable()
                                )
                                .doAfterTerminate(tx::rxCommit)

                )
                .map(this::rowToPost);
    }

    public Maybe<Post> findById(UUID id) {
        return this.client
                .preparedQuery("SELECT * FROM posts WHERE id=$1").rxExecute(Tuple.of(id))
                .map(RowSet::iterator)
                .flatMapMaybe(it -> it.hasNext() ? Maybe.just(rowToPost(it.next())) : Maybe.empty());
    }

    private Post rowToPost(Row row) {
        return Post.of(row.getUUID("id"), row.getString("title"), row.getString("content"), row.getLocalDateTime("created_at"));
    }

    public Single<UUID> save(Post data) {
        return this.client
                .rxBegin()
                .flatMap(
                        tx -> tx.preparedQuery("INSERT INTO posts (title, content) VALUES ($1, $2) RETURNING (id)").rxExecute(Tuple.of(data.getTitle(), data.getContent()))
                                .toFlowable().firstOrError()
                                .doAfterTerminate(tx::rxCommit)
                )
                .map(RowSet::iterator)
                .map(it -> it.hasNext() ? it.next().getUUID("id") : null);
    }

    public Single<Integer> update(UUID id, Post data) {
        return this.client
                .preparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3").rxExecute(Tuple.of(data.getTitle(), data.getContent(), id))
                .map(RowSet::rowCount);
    }

    public Single<Integer> deleteAll() {
        return client.query("DELETE FROM posts").rxExecute()
                .map(RowSet::rowCount);
    }

    public Single<Integer> delete(UUID id) {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1").rxExecute(Tuple.of(id))
                .map(RowSet::rowCount);
    }

}
