package com.example.demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    @GET
    public List<Task> listAll() {
        return Task.listAll();
    }
}