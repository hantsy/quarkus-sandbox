# JAX-RS Client API

The `restclient-jaxrs` module creates an outbound client with
`ClientBuilder`. It uses the JAX-RS Client API directly rather than registering
a MicroProfile REST Client interface.

## Configure and call the service

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-jaxrs/src/main/java/com/example/demo/PostResourceClient.java)
reads its `baseUrl` from `PostServiceProperties`, which maps
`post-service.base_url`. The module's `application.properties` sets that
value to `http://localhost:8080` and listens locally on port `8081`.

The client uses `ClientBuilder`, query parameters, typed entity reads, and
asynchronous JAX-RS invocations that return `CompletionStage`. The POM
includes the RESTEasy Classic Client and Jackson client extensions. See
[Jackson on REST clients](./jackson-client.md).

## Run the client

Start a posts service at the configured base URL, then run the client from
`restclient-jaxrs`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- Compare the declarative API in [MicroProfile REST Client with RESTEasy Classic](./microprofile-classic.md).
- See [Java HTTP Client](./java-http-client.md) for a JDK client.
