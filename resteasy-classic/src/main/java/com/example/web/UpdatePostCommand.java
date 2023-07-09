package com.example.web;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.validation.constraints.NotEmpty;

public record UpdatePostCommand(@NotEmpty String title,
                                @NotEmpty String content) {
    @JsonbCreator
    public UpdatePostCommand {
    }
}
