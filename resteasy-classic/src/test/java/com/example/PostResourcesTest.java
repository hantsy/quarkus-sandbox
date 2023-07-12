package com.example;

import com.example.domain.Post;
import com.example.repository.PostRepository;
import com.example.web.CreatePostCommand;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PostResourcesTest {

    @InjectMock
    PostRepository postRepository;

    @Test
    @DisplayName("get post by id(id does not exist)")
    public void getNoneExistedPost_shouldReturn404() {
        when(this.postRepository.findByIdOptional(any(UUID.class))).thenReturn(Optional.ofNullable(null));
        //@formatter:off
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/posts/{id}", UUID.randomUUID())
        .then()
            .statusCode(404);
        //@formatter:on

        verify(this.postRepository, times(1)).findByIdOptional(any(UUID.class));
        verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    @DisplayName("get post by id")
    public void getExistedPost_shouldReturn200() {
        var id = UUID.randomUUID();
        var data = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").createdAt(LocalDateTime.now()).id(id).build();
        when(this.postRepository.findByIdOptional(any(UUID.class))).thenReturn(Optional.ofNullable(data));

        //@formatter:off
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/posts/{id}", id)
        .then()
            .statusCode(200)
            .log().all()
            .body("title", is("Hello Quarkus"));
        //@formatter:on

        verify(this.postRepository, times(1)).findByIdOptional(any(UUID.class));
        verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    @DisplayName("find all posts")
    public void testFindPostsEndpoint() {
        var data = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").createdAt(LocalDateTime.now()).id(UUID.randomUUID()).build();
        when(this.postRepository.findByKeyword(anyString(), isA(int.class), isA(int.class))).thenReturn(List.of(data));

        //@formatter:off
        given()
            .accept(ContentType.JSON)
            .queryParam("q", "")
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .log().all()
            .body(
                    "size()", is(1),
                    "[0].title", is("Hello Quarkus")
            );
        //@formatter:on

        verify(this.postRepository, times(1)).findByKeyword(anyString(), isA(int.class), isA(int.class));
        verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    @DisplayName("create new post")
    public void createPost() {
        UUID id = UUID.randomUUID();
        doAnswer(invocation -> {
            var p = (Post) (invocation.getArguments()[0]);
            p.setId(id);
            return null;
        }).when(this.postRepository).persist(any(Post.class));

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

        verify(this.postRepository, times(1)).persist(any(Post.class));
        verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    @DisplayName("create new post(invalid input data)")
    public void createPost_invalidInput() {
        UUID id = UUID.randomUUID();
        doAnswer(invocation -> {
            var p = (Post) (invocation.getArguments()[0]);
            p.setId(id);
            return null;
        }).when(this.postRepository).persist(any(Post.class));

        //@formatter:off
        given()
            .body(new CreatePostCommand(null, null))
            .contentType(ContentType.JSON)
        .when()
            .post("/posts")
        .then()
            .statusCode(400)
            .log().all();
        //@formatter:on

       verifyNoInteractions(this.postRepository);
    }

}
