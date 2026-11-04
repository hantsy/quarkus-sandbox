# MicroProfile REST Client with Quarkus REST

The `restclient-reactive` module declares a REST client interface for Quarkus
REST and returns Mutiny `Uni` values for ordinary responses. It also declares
an event-stream method that returns `Multi`.

## Declare and configure the client

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-reactive/src/main/java/com/example/demo/PostResourceClient.java)
uses `@RegisterRestClient` and registers a response exception mapper. It
provides count, list, and by-ID methods, plus an `events` endpoint annotated
for server-sent events.

The module configures the client URL and listens on port `8081`:

```properties
quarkus.http.port=8081
com.example.demo.PostResourceClient/mp-rest/url=http://localhost:8080
com.example.demo.PostResourceClient/mp-rest/scope=jakarta.inject.Singleton
```

The POM includes `quarkus-rest-client` and
`quarkus-rest-client-jackson`. See [Jackson on REST
clients](./jackson-client.md).

## Run the client

Start a posts service on port `8080`, then run the client from
`restclient-reactive`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- Compare `CompletionStage` in [the RESTEasy Classic client](./microprofile-classic.md).
- See [MicroProfile REST Client with Kotlin coroutines](./microprofile-kotlin-coroutines.md).
