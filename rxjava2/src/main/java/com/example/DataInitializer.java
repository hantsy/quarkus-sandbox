package com.example;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PgPool client;

    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Tuple first = Tuple.of("Hello Quarkus", "My first post of Quarkus");
        Tuple second = Tuple.of("Hello Again, Quarkus", "My second post of Quarkus");

        client.rxBegin()
                .flatMapCompletable(tx -> tx
                        .rxQuery("DELETE FROM posts")
                        .flatMap(result -> tx.rxPreparedBatch("INSERT INTO posts (title, content) VALUES ($1, $2)", List.of(first, second)))
                        .flatMap(rs -> tx.rxQuery("SELECT * FROM posts"))
                        .doOnSuccess(rows -> rows.forEach(row -> LOGGER.log(Level.INFO, "inserted post: {0}", row)))
                        .flatMapCompletable(result -> tx.rxCommit())
                )
                .subscribe(
                        () -> {
                            LOGGER.info("data initialization done");
                        },
                        err -> {
                            LOGGER.warning("failed to initialize...");
                        }
                );

    }

    private String readInitScript() {
        try {
            return Files.readString(Paths.get(this.getClass().getResource("/init.sql").toURI()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
