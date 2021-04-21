package com.example.web;

import javax.validation.constraints.NotEmpty;

public record UpdatePostCommand(@NotEmpty String title, @NotEmpty String content) {
}
