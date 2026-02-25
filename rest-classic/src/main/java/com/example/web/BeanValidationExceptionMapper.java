package com.example.web;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static jakarta.ws.rs.core.Response.status;

@Provider
public class BeanValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations()
                .forEach(v -> errors.put(lastFieldName(v.getPropertyPath().iterator()), v.getMessage()));
        return status(Response.Status.BAD_REQUEST).entity(errors).build();
    }

    private String lastFieldName(Iterator<Path.Node> nodes) {
        Path.Node last = null;
        while (nodes.hasNext()) {
            last = nodes.next();
        }
        return last.getName();
    }
}
