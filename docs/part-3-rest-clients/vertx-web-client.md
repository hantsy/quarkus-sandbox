# Vert.x Web Client

The `restclient-classic-vertx` module uses the Mutiny Vert.x Web Client to call the
posts service. Its methods return Mutiny `Uni` or `Multi` values.

## Configure and call the service

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-classic-vertx/src/main/java/com/example/demo/PostResourceClient.java)
creates a Mutiny `WebClient` with the host and port provided by
`PostServiceProperties`. The module's configuration sets the host to
`localhost` and the port to `8080`, and listens locally on `8081`.

The client maps JSON arrays to `Post` values and provides count, list, and
by-ID methods. The POM includes the SmallRye Mutiny Vert.x Web Client
dependency.

## Run the client

Start a posts service on port `8080`, then run the client from
`restclient-classic-vertx`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- Compare [MicroProfile REST Client with Quarkus REST](./microprofile-reactive.md).
- See the JDK approach in [Java HTTP Client](./java-http-client.md).
