package com.example;

import java.time.LocalDateTime;

public class Post {
    String id;
    String title;
    String content;
    LocalDateTime createdAt;

    public static Post of(String title, String content) {
        return Post.of(null, title, content);
    }

    public static Post of(String id, String title, String content) {
        Post data = new Post();
        data.setId(id);
        data.setTitle(title);
        data.setContent(content);
        return data;
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
