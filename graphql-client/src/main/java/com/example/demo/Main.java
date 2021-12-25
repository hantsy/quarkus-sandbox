package com.example.demo;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@QuarkusMain
public class Main implements QuarkusApplication {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Inject
    PostGraphQLClient clientApi;

    @Inject
    PostDynamicClient dynamicClient;

    @Inject
    JvmClient jvmClient;

    @Inject
    JaxrsClient jaxrsClient;

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
        if (post.hasErrors()) {
            post.getErrors().forEach(
                    error -> LOGGER.log(Level.INFO, "error: path={0}, message={1}", new Object[]{error.getPath(), error.getMessage()})
            );
        }


        this.clientApi.getAllPosts().forEach(
                p -> LOGGER.log(Level.INFO, "post: {0}", p)
        );

        this.clientApi.getAllPostSummaries().forEach(
                p -> LOGGER.log(Level.INFO, "post summary: {0}", p)
        );

        this.dynamicClient.getAllPosts().forEach(
                p -> LOGGER.log(Level.INFO, "post from dynamic client: {0}", p)
        );

        this.jvmClient.getAllPosts()
                .thenAccept(
                        p -> LOGGER.log(Level.INFO, "post from jvm client: {0}", p)
                )
                .whenComplete((d, e) -> LOGGER.info("The request is done in the jvm client."))
                .toCompletableFuture()
                .join();

        this.jaxrsClient.getAllPosts()
                .thenAccept(
                        p -> LOGGER.log(Level.INFO, "post from Jaxrs client: {0}", p)
                )
                .whenComplete((d, e) -> LOGGER.info("The request is done in the Jaxrs client."))
                .toCompletableFuture()
                .join();

        return 0;
    }
}
