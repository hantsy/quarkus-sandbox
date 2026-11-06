package com.example.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.SseEventSource;
import org.jboss.resteasy.plugins.providers.StringTextStar;
import org.jboss.resteasy.plugins.providers.sse.SseEventProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;
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
                .register(StringTextStar.class)
                // Quarkus registers the SSE reader on the server side only, so the client needs
                // it explicitly to turn a text/event-stream body into SseEventInputImpl frames
                .register(new SseEventProvider())
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

    /**
     * Subscribes to the {@code text/event-stream} endpoint with the JAX-RS SSE client and
     * collects the events it publishes. {@link SseEventSource#open()} returns once the
     * connection is up and closes the source when the stream ends, so the
     * {@code CompletionStage} settles on the SSE consumer thread.
     */
    CompletionStage<List<Post>> getEvents() {
        CompletableFuture<List<Post>> result = new CompletableFuture<>();
        List<Post> events = new CopyOnWriteArrayList<>();
        SseEventSource eventSource = SseEventSource
                .target(client.target(baseUrl + "/posts/events"))
                .build();

        eventSource.register(
                event -> {
                    if (!event.isEmpty()) {
                        events.add(event.readData(Post.class));
                    }
                },
                result::completeExceptionally,
                () -> result.complete(List.copyOf(events)));

        eventSource.open();
        return result;
    }

}
