package com.example.domain;

import java.util.UUID;

public record PostSummary(UUID id, String title, Integer countOfComments) {
}
