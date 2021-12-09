package com.example;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(UUID id) {
    }
}
