package com.example

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.GenericType
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.Test

@WireMockTest(httpPort = 8080)
@QuarkusTest
class PostResourceClientTest {

    @Inject
    @field:RestClient
    lateinit var client: PostResourceClient

    @Test
    fun `get all posts`() {
        val url = "/posts?q=&offset=0&limit=10"
        stubFor(
            get(url)
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            [
                                {
                                    "id":1,
                                    "title":"test post 1",
                                    "content":"test content of post 1", 
                                    "createdAt":"2023-03-26T10:15:30"
                                },
                                {
                                    "id":2,
                                    "title":"test post 2",
                                    "content":"test content of post 2", 
                                    "createdAt":"2023-03-26T10:15:30"
                                }
                            ]
                            """.trimIndent()
                        )
                )
        )

        val countResponse = client.getAllPosts("")
        countResponse.readEntity(object : GenericType<List<Post>>() {}).size shouldBe 2

        verify(
            getRequestedFor(urlEqualTo(url))
                .withHeader("Accept", equalTo("application/json"))
        )

    }

    @Test
    fun `get count`() {
        val url = "/posts/count?q="
        stubFor(
            get(url)
                .withHeader("Accept", equalTo("text/plain"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("10")
                        .withStatus(200)
                )
        )

        val countResponse = client.countAllPosts("")
        countResponse.readEntity(Integer::class.java) shouldBe 10

        verify(
            getRequestedFor(urlEqualTo(url))
                .withHeader("Accept", equalTo("text/plain"))
        );

    }
}