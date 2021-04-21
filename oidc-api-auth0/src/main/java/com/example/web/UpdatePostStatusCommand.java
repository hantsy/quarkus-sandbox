package com.example.web;

import com.example.domain.Post;

import javax.validation.constraints.NotEmpty;

public record UpdatePostStatusCommand(@NotEmpty Post.Status status) {
}
