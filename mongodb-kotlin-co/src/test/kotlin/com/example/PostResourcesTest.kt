package com.example

import com.example.repository.Post
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.bson.types.ObjectId
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime


@QuarkusTest
class PostResourcesTest {

    @InjectMock
    lateinit var postRepository: PostRepository

    @Test
    fun `get all posts`() {
        `when`(postRepository.streamAll())
            .thenReturn(
                Multi.createFrom().items(
                    Post(ObjectId(), "foo", "bar", LocalDateTime.now()),
                    Post(ObjectId(), "foo2", "bar2", LocalDateTime.now())
                )
            )

        //@formatter:off
        given()
            .accept(ContentType.JSON)
        .`when`()
            .get("/posts")
        .then()
            .statusCode(200)
            .log().all()
            .body("size()", `is`(2),
                "[0].title", `is`("foo")
            )
        //@formatter:on

        verify(postRepository, times(1)).streamAll()
        verifyNoMoreInteractions(postRepository)
    }

    @Test
    fun `get post by id`() {
        val id = ObjectId()
        `when`(postRepository.findById(any(ObjectId::class.java)))
            .thenReturn(
                Uni.createFrom().item(
                    Post(id, "foo", "bar", LocalDateTime.now()),
                )
            )

        //@formatter:off
        given()
            .accept(ContentType.JSON)
        .`when`()
            .get("/posts/$id")
        .then()
            .statusCode(200)
            .log().all()
            .body( "title", `is`("foo"))
        //@formatter:on

        verify(postRepository, times(1)).findById(any(ObjectId::class.java))
        verifyNoMoreInteractions(postRepository)
    }

    @Test
    fun `save post`() {
        val id = ObjectId()
        val post = Post(id, "foo", "bar", LocalDateTime.now())
        `when`(postRepository.persist(any(Post::class.java)))
            .thenReturn(
                Uni.createFrom().item(post)
            )

        //@formatter:off
        given()
            .contentType(ContentType.JSON)
            .body(Post(title="foo", body = "bar"))
        .`when`()
            .post("/posts")
        .then()
            .statusCode(201)
            .log().all()
            .header("location", containsString("/posts/$id"))
        //@formatter:on

        verify(postRepository, times(1)).persist(any(Post::class.java))
        verifyNoMoreInteractions(postRepository)
    }
}