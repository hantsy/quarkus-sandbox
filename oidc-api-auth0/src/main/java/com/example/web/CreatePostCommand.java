package com.example.web;

import javax.validation.constraints.NotEmpty;

public record CreatePostCommand(@NotEmpty String title, @NotEmpty String content) {
}
