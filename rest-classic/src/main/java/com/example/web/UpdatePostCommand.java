package com.example.web;

import jakarta.validation.constraints.NotEmpty;

public record UpdatePostCommand(@NotEmpty String title,
                                @NotEmpty String content) {
}
