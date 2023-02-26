package com.example.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.bind.Jsonb;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class JvmClient {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(executorService)
            .version(HttpClient.Version.HTTP_2)
            .build();


    @Inject
    Jsonb jsonb;

    CompletionStage<List<Post>> getAllPosts() {

        // Have to erase the new line chars in the GraphQL schema to avoid the parsing exception.
        // see: https://github.com/quarkusio/quarkus/issues/17667
        var queryString = """
                {"query": "query {
                            allPosts {
                              id
                              title
                              content
                              comments {
                                id
                                content
                              }
                            }
                          }"
                }
                """.replaceAll("\\n", " ");
        // The following forms are working well.
//        var queryString = """
//            {
//              "query": "query {  allPosts {  id title content comments { id  content } } }"
//            }
//            """;
//
//        var queryString = "{\"query\": \"query { " +
//                "   allPosts { " +
//                "     id " +
//                "     title " +
//                "     content " +
//                "     comments { " +
//                "       id " +
//                "       content " +
//                "     } " +
//                "   } " +
//                " }\"" +
//                "}";

        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(queryString))
                .uri(URI.create("http://localhost:8080/graphql"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return this.httpClient
                .sendAsync(request, handler)
                .thenApply(HttpResponse::body)
                .thenApply(this::extractPosts);

    }

    /**
     * @param s the GraphQL response, eg.
     * @formatter:off
     *          {
     *              data:{
     *                  allPosts: [
     *                      {
     *                      id:"xxxx",
     *                      title:"test title",
     *                      content:"content"
     *                      }
     *                  ]
     *              }
     *          }
     * @formatter:on
     * @return The parsed the list of post data.
     */
    List<Post> extractPosts(String s) {
        var reader = new StringReader(s);
        var json = Json.createReader(reader).read();
        var pointer = Json.createPointer("/data/allPosts");
        var jsonArray = (JsonArray) pointer.getValue(json);
        //@formatter:off
        return jsonb.fromJson(jsonArray.toString(), new TypeLiteral<List<Post>>() {}.getRawType());
        //@formatter:on
    }
}