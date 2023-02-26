package com.example;

import com.example.domain.Post;
import com.example.repository.PostRepository;
import com.example.web.CreatePostCommand;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.annotation.JsonbCreator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PostResourceTest {

    @InjectMock
    PostRepository postRepository;

    @Inject
    Jsonb jsonb;

    @Test
    public void getNoneExistedPost_shouldReturn404() {
        when(this.postRepository.findByIdOptional(anyString()))
                .thenReturn(Optional.ofNullable(null));
        given()
                .when().get("/posts/" + UUID.randomUUID().toString())
                .then()
                .statusCode(404);

        verify(this.postRepository, times(1)).findByIdOptional(anyString());
        verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    public void getExistedPost_shouldReturn200() {
        var data = Post.builder().title("Hello Quarkus").content("My first post of Quarkus")
                .createdAt(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .build();
        when(this.postRepository.findByIdOptional(anyString()))
                .thenReturn(Optional.ofNullable(data));
        given()
                .when().get("/posts/test")
                .then()
                .statusCode(200)
                .log().all()
                .body("title", is("Hello Quarkus"));

        verify(this.postRepository, times(1)).findByIdOptional(anyString());
        verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    public void testPostsEndpoint() {
        var data = Post.builder().title("Hello Quarkus").content("My first post of Quarkus")
                .createdAt(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .build();
        when(this.postRepository.findByKeyword(anyString(), isA(int.class), isA(int.class)))
                .thenReturn(
                        List.of(data)
                );
        given()
                .queryParam("q", "")
                .when().get("/posts")
                .then()
                .statusCode(200)
                .log().all()
                .body(
                        "size()", is(1),
                        "[0].title", is("Hello Quarkus")
                );

        verify(this.postRepository, times(1)).findByKeyword(anyString(), isA(int.class), isA(int.class));
        verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    public void createPost() {
        var data = Post.builder().title("Hello Quarkus").content("My first post of Quarkus")
                .createdAt(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .build();
        when(this.postRepository.save(any(Post.class)))
                .thenReturn(data);
        //@formatter:off
        given()
            .body(new CreatePostCommand("Hello Quarkus", "Test Content"))
            .contentType(ContentType.JSON)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .log().all()
            .header("Location", notNullValue());
        //@formatter:on

        verify(this.postRepository, times(1)).save(any(Post.class));
        verifyNoMoreInteractions(this.postRepository);
    }

}