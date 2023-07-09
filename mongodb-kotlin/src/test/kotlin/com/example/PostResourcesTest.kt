package com.example

import io.kotest.matchers.equals.shouldBeEqual
import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.mockito.Mockito.any
import java.time.LocalDateTime
import java.util.stream.Stream


@QuarkusTest
class PostResourcesTest {

    @InjectMock
    lateinit var postRepository: PostRepository

    @Test
    fun `get all posts`() {
        `when`(postRepository.streamAll())
            .thenReturn(
                Stream.of(
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
                Post(id, "foo", "bar", LocalDateTime.now()),
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
        val postCaptor = ArgumentCaptor.forClass(Post::class.java)
        doAnswer {
            val post = it.arguments[0] as Post
            post.id = id
            null
        }.`when`(postRepository).persist(postCaptor.capture())

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

        assertThat( postCaptor.value.id).isEqualTo(id)
        verify(postRepository, times(1)).persist(any(Post::class.java))
        verifyNoMoreInteractions(postRepository)
    }
}