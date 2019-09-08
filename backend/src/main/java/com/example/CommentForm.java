package com.example;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class CommentForm implements Serializable {

    @NotEmpty
    private String content;

    public static CommentForm of(String content) {
        CommentForm form= new CommentForm();
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
