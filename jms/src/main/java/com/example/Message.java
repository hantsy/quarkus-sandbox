package com.example;

import java.time.Instant;

public record Message(
        String body,
        Instant sentAt
) {
}

