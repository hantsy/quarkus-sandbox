package com.example;

import java.io.Serializable;
import java.util.Objects;

public class PostForm implements Serializable {

    String title;
    String content;

    public static PostForm of(String title, String content) {
        PostForm post = new PostForm();
        post.setTitle(title);
        post.setContent(content);

        return post;
    }


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostForm postForm = (PostForm) o;
        return title.equals(postForm.title) &&
                content.equals(postForm.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }

    @Override
    public String toString() {
        return "PostForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
