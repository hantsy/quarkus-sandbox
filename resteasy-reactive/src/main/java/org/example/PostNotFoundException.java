package org.example;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(UUID uuid) {
        super("Post: " + uuid + " was not found.");
    }
}
