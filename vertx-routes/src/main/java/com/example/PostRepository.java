package com.example;

import io.vertx.axle.pgclient.PgPool;
import io.vertx.axle.sqlclient.Row;
import io.vertx.axle.sqlclient.RowSet;
import io.vertx.axle.sqlclient.SqlResult;
import io.vertx.axle.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public CompletionStage<List<Post>> findAll() {
        return client.query("SELECT * FROM posts ORDER BY id ASC")
                .execute()
                .thenApply(rs -> StreamSupport.stream(rs.spliterator(), false)
                        .map(this::from)
                        .collect(Collectors.toList())
                );
    }

    private Post from(Row row) {
        return Post.of(row.getUUID("id"), row.getString("title"), row.getString("content"), row.getLocalDateTime("created_at"));
    }

    public CompletionStage<Post> findById(UUID id) {
        return client.preparedQuery("SELECT * FROM posts WHERE id=$1").execute(Tuple.of(id))
                .thenApply(RowSet::iterator)
                .thenApply(iterator -> iterator.hasNext() ? from(iterator.next()) : null)
                .thenApply(Optional::ofNullable)
                .thenApply(p -> p.orElseThrow(() -> new PostNotFoundException(id)));
    }

    public CompletionStage<UUID> save(Post data) {
        return client.preparedQuery("INSERT INTO posts(title, content) VALUES ($1, $2) RETURNING (id)").execute(Tuple.of(data.getTitle(), data.getContent()))
                .thenApply(rs -> rs.iterator().next().getUUID("id"));
    }

    public CompletionStage<Integer> update(UUID id, Post data) {
        return client.preparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3").execute(Tuple.of(data.getTitle(), data.getContent(), id))
                .thenApply(SqlResult::rowCount);
    }

    public CompletionStage<Integer> deleteAll() {
        return client.query("DELETE FROM posts").execute()
                .thenApply(SqlResult::rowCount);
    }

    public CompletionStage<Integer> delete(String id) {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1").execute(Tuple.of(UUID.fromString(id)))
                .thenApply(SqlResult::rowCount);
    }

}
