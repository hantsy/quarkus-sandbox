package com.example.demo;


import io.quarkus.runtime.StartupEvent;
import lombok.RequiredArgsConstructor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@ApplicationScoped
@RequiredArgsConstructor
public class DataInitializer {
    public static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
    //
    final PostService postService;

    //
    public void onStartup(@Observes StartupEvent e) {

        var initData = IntStream.range(1, 5).mapToObj(
                i -> {
                    var comments = IntStream.range(1, new Random().nextInt(5)+1).mapToObj(c -> Comment.builder().id(UUID.randomUUID().toString()).content("comment #" + c).build())
                            .toList();
                    var data = Post.builder().title("title #" + i)
                            .id(UUID.randomUUID().toString())
                            .content("test content of #" + i)
                            .comments(comments)
                            .build();
                    return data;
                }
        ).toList();

        this.postService.init(initData);

        this.postService.getAllPosts()
                .forEach(p -> LOGGER.log(Level.INFO, "post data : {0}", p));

    }
}
