package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
class PostsHandler {
    private static final Logger LOGGER = Logger.getLogger(PostsHandler.class.getSimpleName());

    PostRepository posts;

    ObjectMapper objectMapper;

    @Inject
    public PostsHandler(PostRepository posts, ObjectMapper objectMapper) {
        this.posts = posts;
        this.objectMapper = objectMapper;
    }

    public void getAll(RoutingContext rc) {
//        var params = rc.queryParams();
//        var q = params.get("q");
//        var limit = params.get("limit") == null ? 10 : Integer.parseInt(params.get("q"));
//        var offset = params.get("offset") == null ? 0 : Integer.parseInt(params.get("offset"));
//        LOGGER.log(Level.INFO, " find by keyword: q={0}, limit={1}, offset={2}", new Object[]{q, limit, offset});
        this.posts.findAll()
                .collectItems().asList()
                .subscribe().with(data -> rc.response().end(toJson(data)));
    }

    public void get(RoutingContext rc) {
        var params = rc.pathParams();
        var id = params.get("id");
        this.posts.findById(UUID.fromString(id))
                .subscribe()
                .with(
                        post -> rc.response().end(toJson(post)),
                        throwable -> rc.fail(404, throwable)
                );

    }


    public void save(RoutingContext rc) {
        var body = rc.getBodyAsString();
        LOGGER.log(Level.INFO, "request body: {0}", body);
        var form = fromJson(body, PostForm.class);
        this.posts.save(Post.of(form.getTitle(), form.getContent()))
                .subscribe()
                .with(savedId -> {
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

        LOGGER.log(Level.INFO, "\npath param id: {0}\nrequest body: {1}", new Object[]{id, body});
        var form = fromJson(body, PostForm.class);
        this.posts.findById(UUID.fromString(id))
                .onFailure(PostNotFoundException.class).invoke(ex -> rc.response().setStatusCode(404).end())
                .map(
                        post -> {
                            post.setTitle(form.getTitle());
                            post.setContent(form.getContent());

                            return this.posts.update(UUID.fromString(id), post);
                        }
                )
                .subscribe()
                .with(data -> rc.response().setStatusCode(204).end());

    }

    public void delete(RoutingContext rc) {
        var params = rc.pathParams();
        var id = UUID.fromString(params.get("id"));

        this.posts.findById(id)
                .onFailure(PostNotFoundException.class).invoke(ex -> rc.response().setStatusCode(404).end())
                .map(post -> this.posts.delete(id))
                .subscribe()
                .with(data -> rc.response().setStatusCode(204).end());
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
