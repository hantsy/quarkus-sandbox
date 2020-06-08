package com.example;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingRoutes {

//    @Route(path = "/greeting")
//    public void greet(RoutingContext rc) {
//        rc.response().end("Hello Vertx Router!");
//
//    }
//
//    @Route(path = "/greetingTo", methods = HttpMethod.GET)
//    public void greetTo(RoutingExchange re) {
//        re.ok("Hello, " + re.getParam("name").orElse("world"));
//    }
}
