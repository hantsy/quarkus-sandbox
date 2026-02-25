package com.example;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String title;
    String content;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    public static Post of(String title, String content) {
        return Post.builder().title(title).content(content).build();
    }
}
