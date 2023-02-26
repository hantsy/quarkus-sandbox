package com.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import java.time.Instant;

@RequestScoped
@Path("/greeting")
public class GreetingResource {

    @Inject
    GreetingProperties properties;

    @GET()
    @Path("")
    public Response greet(@QueryParam("name") String name) {
        return Response.ok(properties.getText() + ", " + name +" at "+ Instant.now()).build();
    }
}
