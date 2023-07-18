package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.vertx.VertxContextSupport;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
@QuarkusTestResource(
        value = WireMockTestResource.class,
        initArgs = {
                @ResourceArg(
                        name = WireMockTestResource.MOCKED_API_BASE_URL_CONFIG_KEY,
                        value = "com.example.demo.PostResourceClient/mp-rest/url"
                )
        }
)
class PostResourceClientTest {
    private static final Logger LOGGER = Logger.getLogger(PostResourceClientTest.class.getName());

    @InjectWireMock
    WireMockServer wireMockServer;


    @Inject
    @RestClient
    PostResourceClient client;

    @BeforeEach
    void setUp() {
    }

    @Test
    void wireMockServerIsRunning() {
        assertThat(this.wireMockServer.isRunning()).isTrue();
    }

    @Test
    void countAllPosts() throws Throwable {
        var url = "/posts/count?q=";
        stubFor(
                get(url)
                        //.withHeader("Accept", containing("text/plain"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "text/plain")
                                        .withBody("10")
                                        .withStatus(200)
                        )
        );

        var count = VertxContextSupport.subscribeAndAwait(() ->
                client.countAllPosts("")
                        .onItem().invoke(c -> LOGGER.log(Level.INFO, "count is: " + c))
                        .onFailure().invoke(error -> LOGGER.log(Level.INFO, "error: " + error.getMessage()))
        );

        assertThat(count).isEqualTo(10L);

        verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", containing("text/plain"))// it is text/plain;charset=UTF-8
        );
    }

    @Test
    void getAllPosts() throws Throwable {
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

        var postList = VertxContextSupport.subscribeAndAwait(() ->
                client.getAllPosts("", 0, 10)
                        .onItem().invoke(c -> LOGGER.log(Level.INFO, "post is: {0}", c))
                        .onFailure().invoke(error -> LOGGER.log(Level.INFO, "error: {0}", error.getMessage()))

        );

        assertThat(postList.size()).isEqualTo(2);

        verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("application/json"))
        );

    }

    @Test
    void getPostById() throws Throwable {
        var id = "1";
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
                                                                "id":1,
                                                                "title":"test post 1",
                                                                "content":"test content of post 1",
                                                                "createdAt":"2023-03-26T10:15:30"
                                                            }
                                                        """.trim()
                                        )
                        )
        );

        var post = VertxContextSupport.subscribeAndAwait(() ->
                client.getPostById(id)
                        .onItem().invoke(c -> LOGGER.log(Level.INFO, "count is: " + c))
                        .onFailure().invoke(error -> LOGGER.log(Level.INFO, "error: " + error.getMessage()))
        );

        assertThat(post.title()).isEqualTo("test post 1");

        verify(
                getRequestedFor(urlEqualTo(url))
                        .withHeader("Accept", equalTo("application/json"))
        );
    }
}