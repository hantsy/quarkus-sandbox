package com.example

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.common.ResourceArg
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.ws.rs.core.GenericType
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import mockwebserver3.QueueDispatcher
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(
    value = MockWebServerTestResource::class,
    initArgs = [
        ResourceArg(
            name = MockWebServerTestResource.MOCKED_API_BASE_URL_CONFIG_KEY,
            value = "quarkus.rest-client.post-service.url"
        )
    ]
)
class PostResourceClientTest {

    @InjectMockWebServer
    lateinit var mockServer: MockWebServer

    @Inject
    @field:RestClient
    lateinit var client: PostResourceClient

    /**
     * MockWebServer serves its queue in order, so a response left over from an earlier test would
     * be handed to the wrong request. A fresh queue dispatcher empties it between tests.
     */
    @BeforeEach
    fun resetQueue() {
        mockServer.dispatcher = QueueDispatcher()
    }

    @Test
    fun `get all posts`() {
        mockServer.enqueue(
            MockResponse.Builder()
                .code(200)
                .addHeader("Content-Type", "application/json")
                .body(
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
                .build()
        )

        val posts = client.getAllPosts("").readEntity(object : GenericType<List<Post>>() {})
        posts.size shouldBe 2

        val recorded = mockServer.takeRequest()
        recorded.target shouldBe "/posts?q=&offset=0&limit=10"
        recorded.method shouldBe "GET"
        recorded.headers["Accept"].orEmpty() shouldContain "application/json"
    }

    @Test
    fun `get count`() {
        mockServer.enqueue(
            MockResponse.Builder()
                .code(200)
                .addHeader("Content-Type", "text/plain")
                .body("10")
                .build()
        )

        val count = client.countAllPosts("").readEntity(Integer::class.java)
        count shouldBe 10

        val recorded = mockServer.takeRequest()
        recorded.target shouldBe "/posts/count?q="
        recorded.headers["Accept"].orEmpty() shouldContain "text/plain"
    }
}
