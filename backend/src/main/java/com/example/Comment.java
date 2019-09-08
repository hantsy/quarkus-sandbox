package com.example;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Comment implements Serializable {
    private String id;
    private String post;
    private String content;
    private LocalDateTime createdAt;

    public static Comment of(String postId, String content) {
        Comment comment = new Comment();

        comment.setId(UUID.randomUUID().toString());
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(postId);

        return comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
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
        return "Comment{" +
            "id='" + id + '\'' +
            ", post='" + post + '\'' +
            ", content='" + content + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
