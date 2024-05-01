package com.example.demo;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PostService {

    private final static List<Post> STORE = new ArrayList<>();
    private final BroadcastProcessor<PostCreated> processor = BroadcastProcessor.create();

    public void init(List<Post> data) {
        STORE.clear();
        STORE.addAll(data);
    }

    List<Post> getAllPosts() {
        return STORE;
    }

    Post getPostById(String id) {
        return STORE.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    Post createPost(CreatePost postInput) {
        var data = new Post(UUID.randomUUID().toString(),
                postInput.title(),
                postInput.content(),
                null
        );
        STORE.add(data);
        processor.onNext(new PostCreated(data.id(), LocalDateTime.now()));
        return data;
    }

    public Multi<PostCreated> postCreated() {
        return processor;
    }
}
