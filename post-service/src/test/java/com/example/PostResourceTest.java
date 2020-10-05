package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PostResourceTest {

    @InjectMock
    PostRepository postRepository;

    @Test
    public void getNoneExistedPost_shouldReturn404() {
        when(this.postRepository.findByIdOptional(anyString()))
                .thenReturn(Optional.ofNullable(null));
        given()
                .when().get("/posts/"+ UUID.randomUUID().toString())
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
                .when().get("/posts?q=")
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

}