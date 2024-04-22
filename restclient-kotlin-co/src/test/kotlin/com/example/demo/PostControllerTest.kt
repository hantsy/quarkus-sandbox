package com.example.demo

import io.mockk.coEvery
import io.mockk.coVerify
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

@QuarkusTest
class PostControllerTest {

    @InjectMock
    @field:RestClient
    lateinit var client: PostResourceClient

    @Test
    fun getPosts() = runTest {
        coEvery { client.getAllPosts(any(), any(), any()) } returns listOf(
            Post(id = UUID.randomUUID().toString(), title = "foo", content = "bar", createdAt = LocalDateTime.now()),
            Post(id = UUID.randomUUID().toString(), title = "foo2", content = "bar2", createdAt = LocalDateTime.now())
        )
        coEvery { client.countAllPosts(any()) } returns 15L

        //@formatter:off
        given()
            .accept(ContentType.JSON)
        .`when`()
            .get("/api")
        .then()
            .log().all()
            .statusCode(200)
            .body("count", equalTo(15))
        //@formatter:on

        coVerify(exactly = 1) { client.getAllPosts(any(), any(), any()) }
        coVerify(exactly = 1) { client.countAllPosts(any()) }
    }
}