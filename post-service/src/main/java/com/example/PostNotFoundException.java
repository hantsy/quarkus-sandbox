package com.example;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String id) {
        super("Post:" + id + " was not found!");
    }
}
