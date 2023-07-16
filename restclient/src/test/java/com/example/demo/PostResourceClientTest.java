package com.example.demo;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@WireMockTest(httpPort = 8080)
class PostResourceClientTest {

    @Inject
    @RestClient
    PostResourceClient client;

    @BeforeEach
    void setUp() {
    }

    @Test
    void countAllPosts() {
        var url = "/posts/count?q=";
        stubFor(
                get(url)
                        .withHeader("Accept", equalTo("text/plain"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "text/plain")
                                        .withBody("10")
                                        .withStatus(200)
                        )
        );

        client.countAllPosts("")
                .thenAccept(c -> {
                    assertThat(c).isEqualTo(10L);
                })
                .toCompletableFuture().join();

        verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("text/plain"))
        );

    }

    @Test
    void getAllPosts() {
        var url = "/posts?q=&offset=0&limit=10";
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
                                                        """.trim()
                                        )
                        )
        );

        client.getAllPosts("", 0, 10)
                .thenAccept(posts -> {
                    assertThat(posts.size()).isEqualTo(2);
                })
                .toCompletableFuture().join();


        verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("application/json"))
        );
    }

    @Test
    void getPostById() {
        var id = new Random().nextInt();
        var url = "/posts/" + id;
        stubFor(
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


        verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("application/json"))
        );
    }
}