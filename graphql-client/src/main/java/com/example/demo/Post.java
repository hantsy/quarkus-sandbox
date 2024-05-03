package com.example.demo;

import java.util.List;

public record Post(
        String id,
        String title,
        String content,
        Integer countOfComments,
        List<Comment> comments
) {

}
