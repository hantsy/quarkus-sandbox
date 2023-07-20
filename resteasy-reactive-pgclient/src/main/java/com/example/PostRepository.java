package com.example;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final Function<Row, Post> ROW_MAPPER = (Row row) ->
            new Post(
                    row.getUUID("id"),
                    row.getString("title"),
                    row.getString("content"),
                    row.getLocalDateTime("created_at")
            );

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public Multi<Post> findAll() {
        return this.client
                .query("SELECT * FROM posts")
                .execute()
                .onItem().transformToMulti(
                        rs -> Multi.createFrom().items(() -> StreamSupport.stream(rs.spliterator(), false))
                )
                .map(ROW_MAPPER);

    }

    public Uni<Post> findById(UUID id) {
        return this.client
                .preparedQuery("SELECT * FROM posts WHERE id=$1")
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                // .map(it -> it.hasNext() ? rowToPost(it.next()) : null);
                .flatMap(it -> it.hasNext() ? Uni.createFrom().item(ROW_MAPPER.apply(it.next())) : Uni.createFrom().failure(PostNotFoundException::new));
    }


    public Uni<UUID> save(Post data) {
        return this.client
                .preparedQuery("INSERT INTO posts (title, content) VALUES ($1, $2) RETURNING (id)")
                .execute(Tuple.of(data.title(), data.content()))
                .map(RowSet::iterator)
                .map(it -> it.hasNext() ? it.next().getUUID("id") : null);
    }

    public Uni<Integer> update(UUID id, Post data) {
        return this.client
                .preparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3")
                .execute(Tuple.of(data.title(), data.content(), id))
                .map(RowSet::rowCount);
    }

    public Uni<Integer> deleteAll() {
        return client.query("DELETE FROM posts")
                .execute()
                .map(RowSet::rowCount);
    }

    public Uni<Integer> delete(UUID id) {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1")
                .execute(Tuple.of(id))
                .map(RowSet::rowCount);
    }

}
