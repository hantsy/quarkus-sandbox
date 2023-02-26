package com.example.web;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.validation.constraints.NotEmpty;

public record CreateCommentCommand(@NotEmpty String content) {
    @JsonbCreator
    public CreateCommentCommand {
    }

    public static CreateCommentCommand of(String content) {
        CreateCommentCommand form = new CreateCommentCommand(content);
        return form;
    }
}
