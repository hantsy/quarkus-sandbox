package com.example;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.NoSuchElementException;

import static javax.ws.rs.core.Response.status;

@Provider
public class NoSuchElementExceptionnMapper implements ExceptionMapper<NoSuchElementException> {

    @Override
    public Response toResponse(NoSuchElementException exception) {
        var errors = Map.of("code", "not_found");
        return status(Response.Status.NOT_FOUND).entity(errors).build();
    }
}
