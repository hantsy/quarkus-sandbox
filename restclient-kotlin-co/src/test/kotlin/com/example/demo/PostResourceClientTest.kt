package com.example.demo

import com.github.tomakehurst.wiremock.client.WireMock.*
import io.kotest.matchers.shouldBe
import io.quarkiverse.wiremock.devservice.ConnectWireMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
@ConnectWireMock(restClient = "post-service")
class PostResourceClientTest {

    @Inject
    @field:RestClient
    lateinit var client: PostResourceClient

    @BeforeEach
    fun resetStubs() {
        // @ConnectWireMock points the static WireMock DSL at the dev service.
        reset()
    }

    @Test
    fun `get all posts`() = runTest {
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
                                    "id":"1",
                                    "title":"test post 1",
                                    "content":"test content of post 1",
                                    "createdAt":"2023-03-26T10:15:30"
                                },
                                {
                                    "id":"2",
                                    "title":"test post 2",
                                    "content":"test content of post 2",
                                    "createdAt":"2023-03-26T10:15:30"
                                }
                            ]
                            """.trimIndent()
                        )
                )
        )

        val posts = client.getAllPosts("")
        posts.size shouldBe 2

        verify(
            getRequestedFor(urlEqualTo(url))
                .withHeader("Accept", equalTo("application/json"))
        )
    }

    @Test
    fun `get count`() = runTest {
        val url = "/posts/count?q="
        stubFor(
            get(url)
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("10")
                        .withStatus(200)
                )
        )

        val count = client.countAllPosts("")
        count shouldBe 10

        verify(
            getRequestedFor(urlEqualTo(url))
                .withHeader("Accept", containing("text/plain"))
        )
    }
}
