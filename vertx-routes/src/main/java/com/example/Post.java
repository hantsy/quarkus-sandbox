package com.example;

import java.time.LocalDateTime;
import java.util.UUID;

public record Post (
    UUID id,
    String title,
    String content,
    LocalDateTime createdAt) {

    public static Post of(String title, String content) {
        return Post.of(null, title, content, null);
    }

    public static Post of(UUID id, String title, String content, LocalDateTime createdAt) {
        return new Post(id, title, content, createdAt);
    }
}