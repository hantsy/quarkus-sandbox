package com.example.demo;

import org.eclipse.microprofile.graphql.Id;

import java.util.List;

public record Post(
        @Id
        String id,
        String title,
        String content,
        //int countOfComments;
        List<Comment> comments
) {

}
