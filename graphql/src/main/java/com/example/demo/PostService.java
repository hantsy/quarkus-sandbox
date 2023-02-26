package com.example.demo;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PostService {

    static List<Post> STORE = new ArrayList<>();

    public void init(List<Post> data) {
        STORE.clear();
        STORE.addAll(data);
    }

    List<Post> getAllPosts() {
        return STORE;
    }

    Optional<Post> getPostById(String id) {
        return STORE.stream().filter(p -> p.id.equals(id)).findFirst();
    }

    Post createPost(CreatePost postInput) {
        var data = Post.builder().id(UUID.randomUUID().toString())
                .title(postInput.title)
                .content(postInput.content)
                .build();
        STORE.add(data);
        return data;
    }
}
