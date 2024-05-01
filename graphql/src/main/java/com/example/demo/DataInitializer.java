package com.example.demo;


import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    private final PostService postService;

    public void onStartup(@Observes StartupEvent e) {

        var initData = IntStream.range(1, 5).mapToObj(
                i -> {
                    var comments = IntStream.range(1, new Random().nextInt(5) + 1)
                            .mapToObj(c -> new Comment(UUID.randomUUID().toString(), "comment #" + c))
                            .toList();
                    var data = new Post(UUID.randomUUID().toString(),
                            "title #" + i,
                            "test content of #" + i,
                            comments
                    );
                    return data;
                }
        ).toList();

        this.postService.init(initData);

        this.postService.getAllPosts()
                .forEach(p -> log.debug("post data : {}", p));

    }
}
