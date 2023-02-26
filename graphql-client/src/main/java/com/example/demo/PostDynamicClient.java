package com.example.demo;

import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;

@ApplicationScoped
public class PostDynamicClient {

    @Inject
    @GraphQLClient("post-dynamic-client")
    DynamicGraphQLClient dynamicClient;

    public List<Post> getAllPosts() throws ExecutionException, InterruptedException {
        Document query = document(
                operation(
                        field("allPosts",
                                field("id"),
                                field("title"),
                                field("content"),
                                field("comments",
                                        field("id"),
                                        field("content")
                                )
                        )
                )
        );
        Response response = dynamicClient.executeSync(query);
        return response.getList(Post.class, "allPosts");
    }
}
