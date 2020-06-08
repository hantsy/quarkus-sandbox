package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
class PostsHandlers {
    private static final Logger LOGGER = Logger.getLogger(PostsHandlers.class.getSimpleName());

    PostRepository posts;

    ObjectMapper objectMapper;

    @Inject
    public PostsHandlers(PostRepository posts, ObjectMapper objectMapper) {
        this.posts = posts;
        this.objectMapper = objectMapper;
    }

    public void getAll(RoutingContext rc) {
//        var params = rc.queryParams();
//        var q = params.get("q");
//        var limit = params.get("limit") == null ? 10 : Integer.parseInt(params.get("q"));
//        var offset = params.get("offset") == null ? 0 : Integer.parseInt(params.get("offset"));
//        LOGGER.log(Level.INFO, " posts: {0}", this.posts);
//        LOGGER.log(Level.INFO, " find by keyword: q={0}, limit={1}, offset={2}", new Object[]{q, limit, offset});
        this.posts.findAll().thenAccept(
                data -> rc.response()
                        .write(toJson(data))
                        .end()

        );
    }

    public void get(RoutingContext rc) {
        var params = rc.pathParams();
        var id = params.get("id");
        this.posts.findById(UUID.fromString(id))
                .thenAccept(post -> {
                    rc.response().end(toJson(post));
                });

    }


    public void save(RoutingContext rc) {
        var body = rc.getBodyAsString();
        var form = fromJson(body, PostForm.class);
        this.posts.save(Post.of(form.getTitle(), form.getContent()))
                .thenAccept(
                        savedId -> {
                            rc.response()
                                    .putHeader("Location", "/posts/" + savedId)
                                    .setStatusCode(201)
                                    .end();
                        }
                );
    }

    public void update(RoutingContext rc) {
        var params = rc.pathParams();
        var id = params.get("id");
        var body = rc.getBodyAsString();
        var form = fromJson(body, PostForm.class);
        this.posts.findById(UUID.fromString(id))
                .thenApply(
                        post -> {
                            post.setTitle(form.getTitle());
                            post.setContent(form.getContent());

                            return this.posts.save(post);
                        }
                )
                .thenAccept(
                        data -> {
                            rc.response().setStatusCode(204).end();
                        }
                );

    }

    public void delete(RoutingContext rc) {
        var params = rc.pathParams();
        var id = params.get("id");

        this.posts.findById(UUID.fromString(id))
                .thenApply(
                        post -> {
                            return this.posts.delete(id);
                        }
                )
                .thenAccept(
                        data -> {
                            rc.response().setStatusCode(204).end();
                        }
                );
    }

    private <T> T fromJson(String body, Class<T> tClass) {
        try {
            return objectMapper.readValue(body, tClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String toJson(Object post) {
        try {
            return objectMapper.writeValueAsString(post);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
