package com.example

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import java.time.LocalDateTime
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor


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
        `when`(postRepository.findById(any()))
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

        verify(postRepository, times(1)).findById(any())
        verifyNoMoreInteractions(postRepository)
    }

    @Test
    fun `save post`() {
        val id = ObjectId()
        val postCaptor = argumentCaptor<Post>()
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

        assertThat(postCaptor.firstValue.id).isEqualTo(id)
        verify(postRepository, times(1)).persist(any<Post>())
        verifyNoMoreInteractions(postRepository)
    }
}