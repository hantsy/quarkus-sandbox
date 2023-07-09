package com.example;

import com.example.domain.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

public class FakeCommentRepository {
    static Map<String, Comment> data = new ConcurrentHashMap<>();

    public List<Comment> all() {
        return new ArrayList<>(data.values());
    }

    public Comment getById(String id) {
        return data.get(id);
    }

    public Comment save(Comment comment) {
        data.put(comment.getId(), comment);
        return comment;
    }

    public void deleteById(String id) {
        data.remove(id);
    }

    public List<Comment> allByPostId(String id) {
        return data.values().stream().filter(c -> c.getPost().equals(id)).collect(toList());
    }
}
