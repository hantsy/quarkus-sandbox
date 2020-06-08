package com.example;

import io.vertx.ext.web.Router;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class RoutesObserver {

    @Inject PostsHandlers handlers;

    public void route(@Observes Router router) {
        router.get("/posts").produces("application/json").handler(handlers::getAll);
        router.post("/posts").consumes("application/json").handler(handlers::save);
        router.get("/posts/:id").produces("application/json").handler(handlers::get);
        router.put("/posts/:id").consumes("application/json").handler(handlers::update);
        router.delete("/posts/:id").handler(handlers::delete);

        router.get("/hello").handler(rc -> rc.response().end("Hello from my route"));
    }

}

