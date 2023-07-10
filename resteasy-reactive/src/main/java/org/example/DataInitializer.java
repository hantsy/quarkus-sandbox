package org.example;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@Startup
@IfBuildProfile("dev")
public class DataInitializer {
    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PostRepository posts;

    // There is an issue call reactive opertions in the blocking codes.
    // see: https://github.com/quarkusio/quarkus/issues/14044
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Post first = Post.of("Hello Quarkus", "My first post of Quarkus");
        Post second = Post.of("Hello Again, Quarkus", "My second post of Quarkus");

        this.posts.deleteAll()
                .onItem().invoke(l -> LOGGER.log(Level.INFO, "deleted {0} posts.", new Object[]{l}))
                .chain(d -> this.posts.persist(List.of(first, second)))
                .chain(v -> this.posts.findAll().list())
                .subscribe()
                .with(
                        rows -> rows.forEach(r -> LOGGER.log(Level.INFO, "data:{0}", r)),
                        err -> LOGGER.log(Level.SEVERE, "error:{0}", err.toString())
                );
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}


//@ApplicationScoped
//@Startup
//public class DataInitializer {
//    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
//
//    @Inject
//    PgPool client;
//
//    public void onStart(@Observes StartupEvent ev) {
//        LOGGER.info("The application is starting...");
//
//        Tuple first = Tuple.of(UUID.randomUUID(), "Hello Quarkus", "My first post of Quarkus");
//        Tuple second = Tuple.of(UUID.randomUUID(), "Hello Again, Quarkus", "My second post of Quarkus");
//
//        client.query(readInitScript()).execute()
//                .chain(c -> client.query("DELETE FROM posts").execute())
//                .chain(result -> client.preparedQuery("INSERT INTO posts (id, title, content) VALUES ($1, $2)").executeBatch(List.of(first, second)))
//                .chain(rs -> client.query("SELECT * FROM posts").execute())
//                .subscribe()
//                .with(
//                        rows -> rows.forEach(r -> LOGGER.log(Level.INFO, "data:{0}", r)),
//                        err -> LOGGER.log(Level.SEVERE, "error:{0}", err)
//                );
//
//    }
//
//    private String readInitScript() {
//        try {
//            return Files.readString(Paths.get(this.getClass().getResource("/init.sql").toURI()), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    void onStop(@Observes ShutdownEvent ev) {
//        LOGGER.info("The application is stopping...");
//    }
//}
