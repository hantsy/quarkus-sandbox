package com.example.domain;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record PostId(UUID id) {
}
