package com.example.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.github.nilwurtz.GraphqlBodyMatcher;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.ResourceArg;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@QuarkusTestResource(
        value = GraphqlWireMockTestResource.class,
        initArgs = @ResourceArg(
                name = GraphqlWireMockTestResource.MOCKED_API_BASE_URL_CONFIG_KEYS,
                value = "quarkus.smallrye-graphql-client.post-client-typesafe.url,"
                        + "quarkus.smallrye-graphql-client.post-client-dynamic.url")
)
class GraphQLClientsTest {

    // The document the typesafe client generates for getAllPosts. The extension compares the query
    // semantically, so neither the whitespace nor the field order here has to match the request.
    private static final String ALL_POSTS_QUERY =
            "query allPosts { allPosts { id title content countOfComments comments { id content } } }";

    // SmallRye names the operation in the request body, and the extension requires an operationName it is
    // not given to be absent from that body.
    private static final String ALL_POSTS_OPERATION = "allPosts";

    private static final String ALL_POSTS_RESPONSE = """
            {
              "data": {
                "allPosts": [
                  {
                    "id": "1",
                    "title": "Hello GraphQL",
                    "content": "The first post",
                    "countOfComments": 1,
                    "comments": [ { "id": "c1", "content": "Nice read" } ]
                  }
                ]
              }
            }
            """;

    @InjectWireMock
    WireMockServer wireMock;

    @Inject
    PostGraphQLClient typesafeClient;

    @Inject
    PostDynamicClient dynamicClient;

    @BeforeEach
    void resetStubs() {
        wireMock.resetAll();
    }

    @Test
    void typesafeGetAllPosts() {
        stubGraphql(ALL_POSTS_QUERY, ALL_POSTS_OPERATION, ALL_POSTS_RESPONSE);

        List<Post> posts = typesafeClient.getAllPosts();

        assertThat(posts).singleElement().satisfies(post -> {
            assertThat(post.id()).isEqualTo("1");
            assertThat(post.title()).isEqualTo("Hello GraphQL");
            assertThat(post.countOfComments()).isEqualTo(1);
            assertThat(post.comments()).extracting("content").containsExactly("Nice read");
        });

        wireMock.verify(postRequestedFor(urlEqualTo("/graphql")));
    }

    @Test
    void typesafeQueryThatIsNotStubbedFails() {
        stubGraphql("query allPosts { allPosts { id } }", ALL_POSTS_OPERATION, ALL_POSTS_RESPONSE);

        assertThatThrownBy(() -> typesafeClient.getAllPosts())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void dynamicGetAllPosts() throws Exception {
        // The dynamic client builds its document with the GraphQL DSL, selects neither `content` nor
        // `countOfComments`, and sends no variables and no operationName, so the single-argument
        // parameters() is the overload that requires both to be absent from the request body.
        stubGraphql("{ allPosts { id title comments { id content } } }", """
                {
                  "data": {
                    "allPosts": [
                      {
                        "id": "1",
                        "title": "Hello GraphQL",
                        "comments": [ { "id": "c1", "content": "Nice read" } ]
                      }
                    ]
                  }
                }
                """);

        List<Post> posts = dynamicClient.getAllPosts();

        assertThat(posts).singleElement().satisfies(post -> {
            assertThat(post.id()).isEqualTo("1");
            assertThat(post.title()).isEqualTo("Hello GraphQL");
            assertThat(post.comments()).extracting("content").containsExactly("Nice read");
        });
    }

    private void stubGraphql(String query, String operationName, String response) {
        wireMock.stubFor(post(urlEqualTo("/graphql"))
                .andMatching(GraphqlBodyMatcher.extensionName,
                        GraphqlBodyMatcher.parameters(query, Map.of(), operationName))
                .willReturn(okJson(response)));
    }

    private void stubGraphql(String query, String response) {
        wireMock.stubFor(post(urlEqualTo("/graphql"))
                .andMatching(GraphqlBodyMatcher.extensionName, GraphqlBodyMatcher.parameters(query))
                .willReturn(okJson(response)));
    }
}
