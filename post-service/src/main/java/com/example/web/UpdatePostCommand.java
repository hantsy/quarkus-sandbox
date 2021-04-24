package com.example.web;

import javax.json.bind.annotation.JsonbCreator;
import javax.validation.constraints.NotEmpty;

public record UpdatePostCommand(@NotEmpty String title,
                                @NotEmpty String content) {
    @JsonbCreator
    public UpdatePostCommand {
    }
}
