package com.example;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;


/**
 *
 * @author Hantsy Bai<hantsy@gmail.com>
 *
 */
public class PostForm implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static PostForm of(String title, String content) {
        PostForm _postForm = new PostForm();
        _postForm.setTitle(title);
        _postForm.setContent(content);
        return _postForm;
    }
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
