package com.example.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.plugins.providers.StringTextStar;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@ApplicationScoped
public class PostResourceClient {
    private static final Logger LOGGER = Logger.getLogger(PostResourceClient.class.getName());
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Client client;
    private String baseUrl;//= "http://localhost:8080";

    @Inject
    public PostResourceClient(PostServiceProperties properties) {
        baseUrl = properties.baseUrl();
        client = ClientBuilder.newBuilder()
                .register(ResteasyJackson2Provider.class)
                .register(StringTextStar.class)
                .executorService(executorService)
                .build();
    }

    CompletionStage<Long> countAllPosts(String q) {
        return client.target(baseUrl + "/posts/count")
                .queryParam("q", q)
                .request().accept(MediaType.APPLICATION_JSON_TYPE)
                .rx()
                .get(Long.class);
    }

    CompletionStage<List<Post>> getAllPosts(
            String q,
            int offset,
            int limit
    ) {
        return client.target(baseUrl + "/posts")
                .queryParam("q", q)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .request().accept(MediaType.APPLICATION_JSON_TYPE)
                .rx()
                .get(new GenericType<List<Post>>() {
                });
    }

    CompletionStage<Post> getPostById(String id) {
        return client.target(baseUrl + "/posts/" + id)
                .request().accept(MediaType.APPLICATION_JSON_TYPE)
                .rx()
                .get()
                .thenApply(response -> {
                    if (response.getStatus() == 404) {
                        throw new PostNotFoundException(id);
                    }
                    return response.readEntity(Post.class);
                });
    }

}
