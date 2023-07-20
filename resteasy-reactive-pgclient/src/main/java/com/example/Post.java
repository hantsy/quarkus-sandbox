package com.example;

import java.time.LocalDateTime;
import java.util.UUID;

public record Post(
        UUID id,
        String title,
        String content,
        LocalDateTime createdAt) {

    public static Post of(String title, String content) {
        return new Post(null, title, content, null);
    }
}
