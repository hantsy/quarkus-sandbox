package com.example.demo;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@QuarkusMain
public class Main implements QuarkusApplication {

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
            log.debug("found: {}", post.get());
        }
        if (post.hasErrors()) {
            post.getErrors().forEach(
                    error -> log.error("error: path={}, message={}", error.getPath(), error.getMessage())
            );
        }


        this.clientApi.getAllPosts().forEach(
                p -> log.debug("posts from clientApi: {}", p)
        );

        this.clientApi.getAllPostSummaries().forEach(
                p -> log.debug("post summary from clientApi: {}", p)
        );

        this.dynamicClient.getAllPosts().forEach(
                p -> log.debug("post from dynamicClient: {}", p)
        );

        this.jvmClient.getAllPosts()
                .thenAccept(
                        p -> log.debug("post from jvmClient: {}", p)
                )
                .whenComplete((d, e) -> log.debug("The request is done in the jvm client."))
                .toCompletableFuture()
                .join();

        this.jaxrsClient.getAllPosts()
                .thenAccept(
                        p -> log.debug("post from JaxrsClient: {}", p)
                )
                .whenComplete((d, e) -> log.debug("The request is done in the Jaxrs client."))
                .toCompletableFuture()
                .join();

        return 0;
    }
}
