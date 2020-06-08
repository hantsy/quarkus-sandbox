package com.example;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class RoutesObserver {

    @Inject
    PostsHandler handlers;

    public void routes(@Observes Router router) {
        // register BodyHandler globally.
        //router.post().handler(BodyHandler.create());
        router.get("/posts").produces("application/json").handler(handlers::getAll);
        router.post("/posts").consumes("application/json").handler(BodyHandler.create()).handler(handlers::save);
        router.get("/posts/:id").produces("application/json").handler(handlers::get);
        router.put("/posts/:id").consumes("application/json").handler(BodyHandler.create()).handler(handlers::update);
        router.delete("/posts/:id").handler(handlers::delete);

        router.get("/hello").handler(rc -> rc.response().end("Hello from my route"));
    }

}

