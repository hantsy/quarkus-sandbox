package com.example.demo;

import java.time.Instant;
import java.time.LocalDateTime;

public record PostCreated(String id, LocalDateTime createdAt) {
}
