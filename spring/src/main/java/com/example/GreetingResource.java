package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
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
