package com.example;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class AppInitializer {
    private final static Logger LOGGER = Logger.getLogger(AppInitializer.class.getName());

    @Inject
    private PgPool client;

    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        LOGGER.info("pg client::" + client);

        Tuple first = Tuple.of("Hello Quarkus", "My first post of Quarkus");
        Tuple second = Tuple.of("Hello Again, Quarkus", "My second post of Quarkus");



        this.client
                .rxBegin()
                .flatMap(
                        tx -> tx.rxQuery("DELETE FROM posts")
                                .flatMap(
                                        result -> tx.rxPreparedBatch("INSERT INTO posts (title, content) VALUES ($1, $2)", List.of(first, second))
                                                .flatMap(
                                                        result2 -> tx.rxQuery("SELECT * FROM posts")

                                                )

                                                .doAfterTerminate(tx::commit)

                                ))
                .<List<Post>>map(rs -> {
                    var posts = new ArrayList<Post>();
                    rs.forEach(
                            it -> {
                                posts.add(Post.of(it.getUUID("id").toString(), it.getString("title"), it.getString("content")));
                            }
                    );

                    return posts;
                })
                .subscribe(data -> LOGGER.info("data::" + data));

    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
