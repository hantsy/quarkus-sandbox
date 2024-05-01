package com.example.demo;

import java.util.List;

public record Post(
        String id,
        String title,
        String content,
        int countOfComments,
        List<Comment> comments
) {

}
