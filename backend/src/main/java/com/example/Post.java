package com.example;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Post implements Serializable {

    String id;
    String title;
    String content;
    LocalDateTime createdAt;

    public static Post of(String title, String content) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setCreatedAt(LocalDateTime.now());
        post.setTitle(title);
        post.setContent(content);

        return post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
