package com.example.demo;

import io.smallrye.graphql.api.ErrorCode;

@ErrorCode("POST_NOT_FOUND")
public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(String id) {
        super("Post: "+ id +" was not found.");
    }
}
