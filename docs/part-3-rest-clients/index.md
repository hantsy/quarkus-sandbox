# Part 3: REST clients

The client modules call a posts service, but use different client APIs,
serialization extensions, and asynchronous return types. Start the posts
service in one terminal and a client in another. Check each sample's
`application.properties` for the expected service URL and client port.

## MicroProfile REST Client

- [RESTEasy Classic client](./microprofile-classic.md)
- [Quarkus REST client](./microprofile-reactive.md)
- [Kotlin interface client](./microprofile-kotlin.md)
- [Kotlin coroutine client](./microprofile-kotlin-coroutines.md)

## Programmatic clients

- [JAX-RS Client API](./jaxrs-client.md)
- [Java HTTP Client](./java-http-client.md)
- [Vert.x Web Client](./vertx-web-client.md)

## Client serialization

- [JSON-B on REST clients](./jsonb-client.md)
- [Jackson on REST clients](./jackson-client.md)

## Run a client

Start a posts service such as `rest` on the port expected by the client, usually
`8080`. Then run the client from its own module directory:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- See [GraphQL clients](../part-4-graphql/index.md) for GraphQL APIs.
- Return to [REST backends and data access](../part-2-rest-backends/index.md).
