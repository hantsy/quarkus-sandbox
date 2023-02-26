package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.json.bind.JsonbBuilder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class PostResourceClient {

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(executorService)
            .version(HttpClient.Version.HTTP_2)
            .build();


    public PostResourceClient() {
    }

    CompletionStage<Long> countAllPosts(String q) {
       return  this.httpClient
                .sendAsync(
                        HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create("http://localhost:8080/posts/count?q=" + q))
                                .header("Accept", "application/json")
                                .build()
                        ,
                        HttpResponse.BodyHandlers.ofString()
                )
                .thenApply(HttpResponse::body)
                .thenApply(Long::parseLong)
                .toCompletableFuture();

    }

    CompletionStage<List<Post>> getAllPosts(
            String q,
            int offset,
            int limit
    ) {
        return  this.httpClient
                .sendAsync(
                        HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create("http://localhost:8080/posts?q=" + q + "&offset=" + offset + "&limit=" + limit))
                                .header("Accept", "application/json")
                                .build()
                        ,
                        HttpResponse.BodyHandlers.ofString()
                )
                .thenApply(HttpResponse::body)
                .thenApply(stringHttpResponse -> JsonbBuilder.newBuilder().build().fromJson(stringHttpResponse, new TypeLiteral<List<Post>>() {}.getType()))
                .thenApply(data ->(List<Post>)data)
                .toCompletableFuture();
    }

}
