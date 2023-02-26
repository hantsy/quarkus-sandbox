package com.example;

import java.time.LocalDateTime;

public record Post(Long id,
                   String title,
                   String content,
                   LocalDateTime createdAt) {
}