package com.example.web;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(UUID id) {
        super("Post:" + id + " was not found!");
    }
}
