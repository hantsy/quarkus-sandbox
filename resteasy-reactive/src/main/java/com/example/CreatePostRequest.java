package com.example;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        String content
) {
}
