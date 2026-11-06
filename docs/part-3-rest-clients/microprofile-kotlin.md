# MicroProfile REST Client with Kotlin

The `restclient-classic-kotlin` module declares a MicroProfile REST Client in Kotlin.
Its methods return JAX-RS `Response` values, allowing the caller to inspect
the response directly.

## Inspect the client interface

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-classic-kotlin/src/main/kotlin/com/example/PostResourceClient.kt)
uses `@RegisterRestClient(configKey = "post-service")` and declares methods
for posts and counts. Its POM includes the RESTEasy Classic REST client with
Jackson.

The module sets port `8081` and points the client at `http://localhost:8080`
through the `configKey`:

```properties
quarkus.http.port=8081
quarkus.rest-client.post-service.url=http://localhost:8080
quarkus.rest-client.post-service.scope=jakarta.inject.Singleton
```

The test replaces the posts service with OkHttp `MockWebServer`. A
`QuarkusTestResourceLifecycleManager` starts the server on a dynamic port and
feeds that URL into `quarkus.rest-client.post-service.url`; the test injects
the server with `@InjectMockWebServer` and enqueues `MockResponse` responses
instead of declaring stubs. See [Mock HTTP
peers](../../README.md#mock-http-peers).

## Run the client

Start a posts service at the configured URL, then run the client from
`restclient-classic-kotlin`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- See [the Kotlin coroutine client](./microprofile-kotlin-coroutines.md) for
  suspending interface methods.
- See [Jackson on REST clients](./jackson-client.md) for its JSON extension.
