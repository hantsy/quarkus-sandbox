# MicroProfile REST Client with Kotlin coroutines

The `restclient-kotlin-co` module declares a MicroProfile REST Client using
Kotlin `suspend` methods. Use it to compare coroutine-based clients with
`CompletionStage`, `Uni`, and ordinary response types.

## Inspect and configure the client

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-kotlin-co/src/main/kotlin/com/example/demo/PostResourceClient.kt)
declares suspending methods that return a list of posts and a count. The POM
includes Quarkus REST, REST Client, Jackson, and Kotlin support.

The module sets port `8081` and points the client at `http://localhost:8080`
through the `configKey` declared on `@RegisterRestClient`:

```properties
quarkus.http.port=8081
quarkus.rest-client.post-service.url=http://localhost:8080
quarkus.rest-client.post-service.scope=jakarta.inject.Singleton
```

A simple `configKey` is required for testing: the Quarkiverse WireMock dev
service repoints `quarkus.rest-client.<configKey>.url` when the test class is
annotated with `@ConnectWireMock(restClient = "post-service")`. See [Mock HTTP
peers](../../README.md#mock-http-peers).

## Run the client

Start the posts service, then run the client from `restclient-kotlin-co`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- Compare the Kotlin interface that returns JAX-RS responses in
  [MicroProfile REST Client with Kotlin](./microprofile-kotlin.md).
- See [Jackson on REST clients](./jackson-client.md).
