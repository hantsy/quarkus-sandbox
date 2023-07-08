package com.example;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PostControllerTest {

    @InjectMock
    PostRepository postRepository;

    @Test
    public void testGetAll() {
        when(this.postRepository.findAll(isA(Pageable.class)))
                .thenReturn(
                        new PageImpl<Post>(
                                List.of(
                                        Post.of(UUID.randomUUID(), "foo", "bar"),
                                        Post.of(UUID.randomUUID(), "foo2", "bar2")
                                )
                        )
                );

        //@formatter:off
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .log().all()
            .body("content[0].title", equalTo("foo"));
        //@formatter:on

        verify(this.postRepository, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(this.postRepository);
    }

}