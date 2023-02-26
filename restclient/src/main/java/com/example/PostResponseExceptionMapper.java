package com.example;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import jakarta.ws.rs.core.Response;

public class PostResponseExceptionMapper implements ResponseExceptionMapper<RuntimeException> {
    @Override
    public RuntimeException toThrowable(Response response) {
        if(response.getStatus()==404) {
            return  new PostNotFoundException("post not found, original cause:" + response.readEntity(String.class));
        }

        return null;
    }
}
