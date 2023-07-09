package com.example


import io.kotest.matchers.comparables.shouldNotBeEqualComparingTo
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bson.types.ObjectId
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.stream.Stream


@QuarkusTest
class PostResourcesWithQuarkusMockkTest {

//    companion object {
//        val postRepository: PostRepository = mockk()
//
//        @BeforeAll
//        @JvmStatic
//        fun setupAll() {
//            QuarkusMock.installMockForType(postRepository, PostRepository::class.java)
//        }
//    }
//
//    @BeforeEach
//    fun setup() {
//        clearMocks(postRepository)
//    }

    @InjectMock  // use io.quarkiverse.test.junit.mockk.InjectMock
    lateinit var postRepository: PostRepository

    @Test
    fun `get all posts`() {
        every { postRepository.streamAll() } returns Stream.of(
            Post(ObjectId(), "foo", "bar", LocalDateTime.now()),
            Post(ObjectId(), "foo2", "bar2", LocalDateTime.now())
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

        verify(exactly = 1) { postRepository.streamAll() }
    }

    @Test
    fun `get post by id`() {
        val id = ObjectId()
        every { postRepository.findById(any()) } returns Post(id, "foo", "bar", LocalDateTime.now())

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

        verify(exactly = 1) { postRepository.findById(any()) }
    }

    @Test
    fun `save post`() {
        val id = ObjectId()
        val slot = slot<Post>()
        every { postRepository.persist(capture(slot)) } answers {
            val post = firstArg<Post>()
            post.id = id
        }

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

        slot.captured.id?.shouldBeEqual(id)

        verify(exactly = 1) { postRepository.persist(any<Post>()) }
    }
}