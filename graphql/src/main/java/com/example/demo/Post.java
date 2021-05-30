package com.example.demo;

import lombok.*;
import org.eclipse.microprofile.graphql.Id;
import org.eclipse.microprofile.graphql.Type;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@ToString
@Type
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    String id;
    String title;
    String content;

    @Builder.Default
    List<Comment> comments = new ArrayList<>();
}
