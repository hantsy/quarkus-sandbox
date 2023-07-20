package com.example;


import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PostResourceTest {

    @InjectMock
    private PostRepository postRepository;

    @Test
    public void testGetAll() {
        when(this.postRepository.findAll())
                .thenReturn(
                        Uni.createFrom().item(
                                List.of(
                                        Post.of(UUID.randomUUID(), "foo", "bar", LocalDateTime.now()),
                                        Post.of(UUID.randomUUID(), "foo2", "bar2", LocalDateTime.now())
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
            .body("size()", is(2),
                    "[0].title", is("foo")
            );
        //@formatter:on

        verify(this.postRepository, times(1)).findAll();
        verifyNoMoreInteractions(this.postRepository);
    }
}
