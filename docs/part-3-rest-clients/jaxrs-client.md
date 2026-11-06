# JAX-RS Client API

The `restclient-classic-jaxrs` module creates an outbound client with
`ClientBuilder`. It uses the JAX-RS Client API directly rather than registering
a MicroProfile REST Client interface.

## Configure and call the service

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-classic-jaxrs/src/main/java/com/example/demo/PostResourceClient.java)
reads its `baseUrl` from `PostServiceProperties`, which maps
`post-service.base_url`. The module's `application.properties` sets that
value to `http://localhost:8080` and listens locally on port `8081`.

The client uses `ClientBuilder`, query parameters, typed entity reads, and
asynchronous JAX-RS invocations that return `CompletionStage`. The POM
includes the RESTEasy Classic Client and Jackson client extensions. See
[Jackson on REST clients](./jackson-client.md).

The client also exposes an SSE endpoint through `SseEventSource`, registering
`SseEventProvider` on the `Client` so `text/event-stream` responses can be
read.

## Mock the service in tests

`WireMockTestResource` implements `QuarkusTestResourceLifecycleManager`: it
starts a `WireMockServer` on a dynamic port and returns that URL for the
`post-service.base_url` key named by a `@ResourceArg` on
`@QuarkusTestResource`. The test injects the server with `@InjectWireMock` and
calls `stubFor`, `verify`, and `resetAll` on that instance rather than the
static DSL, because the DSL's default client is thread-local and would keep
pointing at port `8080`.

The SSE test is `@Disabled`. WireMock buffers the whole response body, so a
simulated `text/event-stream` never reaches end-of-stream and `SseEventSource`
fires neither its event nor its completion consumer; `chunkedDribbleDelay` and
a `Connection: close` header were both tried. See [Mock HTTP
peers](../../README.md#mock-http-peers).

## Run the client

Start a posts service at the configured base URL, then run the client from
`restclient-classic-jaxrs`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- Compare the declarative API in [MicroProfile REST Client with RESTEasy Classic](./microprofile-classic.md).
- See [Java HTTP Client](./java-http-client.md) for a JDK client.
