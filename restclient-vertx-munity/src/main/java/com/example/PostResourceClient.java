package com.example;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class PostResourceClient {
    private static final Logger LOGGER = Logger.getLogger(PostResourceClient.class.getName());
    private WebClient client;

    @Inject
    public PostResourceClient(PostServiceProperties properties, Vertx vertx) {
        this.client = WebClient.create(
                vertx,
                new WebClientOptions()
                        .setDefaultHost(properties.getHost())
                        .setDefaultPort(properties.getPort())
                        .setSsl(false)
        );
    }

    Uni<Long> countAllPosts(String q) {
        return client.get("/posts/count")
                .send()
                .map(resp -> {
                            LOGGER.log(Level.FINE, "response of posts/count: {}", resp);
                            return resp.bodyAsJson(Long.class);
                        }
                );
    }

    Multi<Post> getAllPosts(
            String q,
            int offset,
            int limit
    ) {
        return client.get("/posts")
                .addQueryParam("q", q)
                .addQueryParam("offset", "" + offset)
                .addQueryParam("limit", "" + limit)
                .send()
                .onItem().produceMulti(resp ->
                        Multi.createFrom().items(resp.bodyAsJsonArray().stream())
                )
                .onItem().apply(o -> ((JsonObject) o).mapTo(Post.class));
    }


}
