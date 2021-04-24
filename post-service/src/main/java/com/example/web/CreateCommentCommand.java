package com.example.web;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class CreateCommentCommand implements Serializable {

    @NotEmpty
    private String content;

    public static CreateCommentCommand of(String content) {
        CreateCommentCommand form= new CreateCommentCommand();
        form.setContent(content);
        return form;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
