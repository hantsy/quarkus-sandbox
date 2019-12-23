package com.example;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.vertx.axle.pgclient.PgPool;
import io.vertx.axle.sqlclient.Row;
import io.vertx.axle.sqlclient.RowSet;
import io.vertx.axle.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

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
                .thenApply(rs -> {
                    var list = new ArrayList<Post>(rs.size());
                    rs.forEach(row -> list.add(from(row)));
                    return list;
                });
    }

    private Post from(Row row) {
        return Post.of(row.getUUID("id").toString(), row.getString("title"), row.getString("content"));
    }

    public CompletionStage<Post> findById(String id) {
        return client.preparedQuery("SELECT * FROM posts WHERE id=$1", Tuple.of(UUID.fromString(id)))
                .thenApply(RowSet::iterator)
                .thenApply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public CompletionStage<UUID> save(Post data) {
        return client.preparedQuery("INSERT INTO posts (title, content) VALUES ($1, $2) RETURNING (id)", Tuple.of(data.getTitle(), data.getContent()))
                .thenApply(rs -> rs.iterator().next().getUUID("id"));
    }

    public CompletionStage<Boolean> delete(String id) {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1", Tuple.of(UUID.fromString(id)))
                .thenApply(rs -> rs.rowCount() == 1);
    }

}
