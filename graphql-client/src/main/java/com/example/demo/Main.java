package com.example.demo;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientException;

import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@QuarkusMain
public class Main implements QuarkusApplication {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Inject
    PostGraphQLClient clientApi;

    @Override
    public int run(String... args) throws Exception {
        String id = UUID.randomUUID().toString();
        // catch a GraphQLClientException.
//        try {
//            var p = this.clientApi.getPostById(id);
//            LOGGER.log(Level.INFO, "post: {0}", p);
//        } catch (GraphQLClientException e) {
//            if (e.getErrors().stream().anyMatch(error -> error.getErrorCode().equals("POST_NOT_FOUND"))) {
//                throw new PostNotFoundException(id);
//            }
//        }

        // return a `ErrorOr` instead.
        var post = this.clientApi.getPostById(id);
        if (post.isPresent()) {
            LOGGER.log(Level.INFO, "found: {0}", post.get());
        }
        if (post.isError()) {
            post.getErrors().forEach(
                    error -> LOGGER.log(Level.INFO, "error: code={0}, message={1}", new Object[]{error.getErrorCode(), error.getMessage()})
            );
        }


        this.clientApi.getAllPosts().forEach(
                p -> LOGGER.log(Level.INFO, "post: {0}", p)
        );

        return 0;
    }
}
