package com.example.demo;

import java.io.Serializable;
import java.time.LocalDateTime;

public record Post(String id,
                   String title,
                   String content,
                   LocalDateTime createdAt) implements Serializable {

    public static Post of(String title, String content) {
        return new Post(null, title, content, LocalDateTime.now());
    }
}
