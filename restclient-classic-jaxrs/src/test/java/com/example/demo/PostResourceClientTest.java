package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(
        value = WireMockTestResource.class,
        initArgs = @ResourceArg(
                name = WireMockTestResource.MOCKED_API_BASE_URL_CONFIG_KEY,
                value = "post-service.base_url"
        )
)
// Stubs go through the injected WireMockServer rather than the static WireMock DSL: the DSL's
// default client is an InheritableThreadLocal bound to whichever thread configured it, so it
// would keep dialling localhost:8080 from the test thread.
class PostResourceClientTest {

    @InjectWireMock
    WireMockServer wireMockServer;

    @Inject
    PostResourceClient client;

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
    }

    @Test
    void countAllPosts() {
        var url = "/posts/count?q=";
        wireMockServer.stubFor(
                get(url)
                        .withHeader("Accept", equalTo("application/json"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("10")
                                        .withStatus(200)
                        )
        );

        client.countAllPosts("")
                .thenAccept(c -> {
                    assertThat(c).isEqualTo(10L);
                })
                .toCompletableFuture().join();

        wireMockServer.verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("application/json"))
        );

    }

    @Test
    void getAllPosts() {
        var url = "/posts?q=&offset=0&limit=10";
        wireMockServer.stubFor(
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
                                                        """.trim()
                                        )
                        )
        );

        client.getAllPosts("", 0, 10)
                .thenAccept(posts -> {
                    assertThat(posts.size()).isEqualTo(2);
                })
                .toCompletableFuture().join();


        wireMockServer.verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("application/json"))
        );
    }

    @Test
    void getPostById() {
        var id = new Random().nextInt();
        var url = "/posts/" + id;
        wireMockServer.stubFor(
                get(url)
                        .withHeader("Accept", equalTo("application/json"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(
                                                """
                                                        {
                                                                "id":%d,
                                                                "title":"test post 1",
                                                                "content":"test content of post 1",
                                                                "createdAt":"2023-03-26T10:15:30"
                                                            }
                                                        """.trim().formatted(id)
                                        )
                        )
        );

        client.getPostById(String.valueOf(id))
                .thenAccept(post -> {
                    assertThat(post.title()).isEqualTo("test post 1");
                })
                .toCompletableFuture().join();


        wireMockServer.verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("application/json"))
        );
    }

    // see: https://wiremock.org/docs/getting-started/ and
    // https://www.linkedin.com/pulse/wiremock-sse-simulation-arkadii-osheev-jeygf/
    @Disabled("WireMock buffers the response body, so the SSE stream never reaches an end-of-stream: "
            + "RESTEasy's SseEventSource fires neither the event nor the completion consumer. "
            + "ChunkedDribbleDelay and a Connection: close header were both tried and the request still hangs.")
    @Test
    void getEvents() throws Exception {
        var url = "/posts/events";
        // WireMock has no SSE support, so the stub answers with a plain body written in the SSE
        // wire format: an optional "event:" line, one or more "data:" lines, then a blank line
        // that terminates the frame. The stream must end with that blank line, otherwise the
        // last frame is never dispatched, which is why the frames are concatenated explicitly
        // instead of written as a text block whose trailing newline the compiler strips.
        String dataStream = "event: post\n"
                + "data:{\"id\":\"1\",\"title\":\"test post 1\",\"content\":\"test content of post 1\",\"createdAt\":\"2023-03-26T10:15:30\"}\n\n"
                + "event: post\n"
                + "data:{\"id\":\"2\",\"title\":\"test post 2\",\"content\":\"test content of post 2\",\"createdAt\":\"2023-03-26T10:15:31\"}\n\n";

        wireMockServer.stubFor(
                get(url)
                        .withHeader("Accept", containing("text/event-stream"))
                        .willReturn(
                                okForContentType("text/event-stream", dataStream)
                                        // dribbling the body out chunk by chunk is what turns the
                                        // stub into a stream rather than one buffered response
                                        .withChunkedDribbleDelay(2, 200)
                                        // an SSE client only learns the stream ended from EOF, so ask
                                        // for the connection to be closed once the body is written
                                        .withHeader("Connection", "close")
                        )
        );

        var events = client.getEvents().toCompletableFuture().get(10, TimeUnit.SECONDS);

        assertThat(events).hasSize(2);
        assertThat(events.get(0).title()).isEqualTo("test post 1");
        assertThat(events.get(1).title()).isEqualTo("test post 2");

        wireMockServer.verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", containing("text/event-stream"))
        );
    }
}