package com.example.demo;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@QuarkusMain
public class Main implements QuarkusApplication {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Inject
    PostGraphQLClient clientApi;

    @Override
    public int run(String... args) throws Exception {

        this.clientApi.getAllPosts().forEach(
                p -> LOGGER.log(Level.INFO, "post: {0}", p)
        );

        return 0;
    }
}
