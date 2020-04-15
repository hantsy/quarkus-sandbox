package com.example;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public Uni<List<Post>> findAll() {
        return this.client
                .query("SELECT * FROM posts")
                .map(rs -> {
                    var result = new ArrayList<Post>(rs.size());
                    for (Row row : rs) {
                        result.add(this.rowToPost(row));
                    }
                    return result;
                });
    }

    public Uni<Post> findById(UUID id) {
        return this.client
                .preparedQuery("SELECT * FROM posts WHERE id=$1", Tuple.of(id))
                .map(RowSet::iterator)
                .map(it -> it.hasNext() ? rowToPost(it.next()) :null);
               // .flatMap(it -> it.hasNext() ? Uni.createFrom().item(rowToPost(it.next())) : Uni.createFrom().failure(()-> new PostNotFoundException()));
    }

    private Post rowToPost(Row row) {
        return Post.of(row.getUUID("id"), row.getString("title"), row.getString("content"), row.getLocalDateTime("created_at"));
    }

    public Uni<UUID> save(Post data) {
        return this.client
                .preparedQuery("INSERT INTO posts (title, content) VALUES ($1, $2) RETURNING (id)", Tuple.of(data.getTitle(), data.getContent()))
                .map(RowSet::iterator)
                .map(it -> it.hasNext() ? it.next().getUUID("id") : null);
    }

    public Uni<Integer> update(UUID id, Post data) {
        return this.client
                .preparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3", Tuple.of(data.getTitle(), data.getContent(), id))
                .map(RowSet::rowCount);
    }

    public Uni<Integer> deleteAll() {
        return client.query("DELETE FROM posts")
                .map(RowSet::rowCount);
    }

    public Uni<Integer> delete(UUID id) {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1", Tuple.of(id))
                .map(RowSet::rowCount);
    }

}
