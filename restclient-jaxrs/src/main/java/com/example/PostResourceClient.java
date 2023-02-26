package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
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
                .executorService(executorService)
                .build();
    }

    CompletionStage<Long> countAllPosts(String q) {
        return client.target(baseUrl + "/posts/count")
                .queryParam("q", q)
                .request()
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
                .request()
                .rx()
                .get(new GenericType<List<Post>>() {
                });
    }

    Post getPostById(String id) {
        try (Response getPostByIdResponse = client.target(baseUrl + "/posts/" + id)
                .request().get()) {
            if (getPostByIdResponse.getStatus() == 404) {
                throw new PostNotFoundException(id);
            }

            return getPostByIdResponse.readEntity(Post.class);
        }

    }

}
