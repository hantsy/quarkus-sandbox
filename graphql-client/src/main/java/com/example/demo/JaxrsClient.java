package com.example.demo;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.bind.Jsonb;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class JaxrsClient {
    private static final Logger LOGGER = Logger.getLogger(JaxrsClient.class.getName());
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Client client;
    private String baseUrl = "http://localhost:8080/graphql";

    @Inject
    Jsonb jsonb;

    public JaxrsClient() {
        client = ClientBuilder.newBuilder()
                .executorService(executorService)
                .build();
    }

    CompletionStage<List<Post>> getAllPosts() {
        var queryString = """
                {
                  "query": "query {  allPosts {  id title content comments { id  content } } }"
                }
                """;
        return client.target(baseUrl)
                .request().accept(MediaType.APPLICATION_JSON_TYPE)
                .rx()
                .post(Entity.entity(queryString, MediaType.APPLICATION_JSON_TYPE), String.class)
                .thenApply(this::extractPosts);
    }

    List<Post> extractPosts(String s) {
        LOGGER.log(Level.INFO, "response of jaxrs client: {0}", s);
        var reader = new StringReader(s);
        var json = Json.createReader(reader).read();
        var pointer = Json.createPointer("/data/allPosts");
        var jsonArray = (JsonArray) pointer.getValue(json);
        //@formatter:off
        return jsonb.fromJson(jsonArray.toString(), new TypeLiteral<List<Post>>() {}.getRawType());
        //@formatter:on
    }
}
