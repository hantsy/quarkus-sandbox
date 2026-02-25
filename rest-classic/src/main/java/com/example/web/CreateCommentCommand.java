package com.example.web;

import jakarta.validation.constraints.NotEmpty;

public record CreateCommentCommand(@NotEmpty String content) {
    public static CreateCommentCommand of(String content) {
        return new CreateCommentCommand(content);
    }
}
