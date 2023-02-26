package com.example;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PostNotFoundExceptionMapper implements ExceptionMapper<PostNotFoundException> {
    @Override
    public Response toResponse(PostNotFoundException exception) {
        return Response.status(404).entity(exception.getMessage()).build();
    }
}
