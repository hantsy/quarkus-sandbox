package com.example;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class PostNotFoundExceptionMapper implements ExceptionMapper<PostNotFoundException> {
    @Override
    public Response toResponse(PostNotFoundException exception) {
        return status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
    }
}
