package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    String id;

    @NotEmpty
    String title;

    @NotEmpty
    String content;

    @Enumerated(EnumType.STRING)
    Status status = Status.DRAFT;

    @Builder.Default
    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    LocalDateTime updatedAt;

    public static enum Status{
        DRAFT,
        PENDING_MODERATED,
        APPROVED,
        REJECTED
    }
}
