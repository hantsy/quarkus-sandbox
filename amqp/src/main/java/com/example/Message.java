package com.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Message {
    String body;
    Instant sentAt;
}
