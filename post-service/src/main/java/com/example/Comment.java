package com.example;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Comment implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "post_id")
    )
    private PostId post;
    private String content;
    private LocalDateTime createdAt;

    public static Comment of(String postId, String content) {
        Comment comment = new Comment();

        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(new PostId(postId));

        return comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PostId getPost() {
        return post;
    }

    public void setPost(PostId post) {
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
