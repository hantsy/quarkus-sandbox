# MicroProfile REST Client with RESTEasy Classic

The `restclient-classic` module declares its posts client as a JAX-RS
interface and runs the client with RESTEasy Classic. Its methods return
`CompletionStage` values.

## Declare the client

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-classic/src/main/java/com/example/demo/PostResourceClient.java)
uses `@RegisterRestClient(configKey = "post-service")` and `@RegisterProvider`
for its response exception mapper. It exposes count, list, and by-ID
operations. Its POM includes `quarkus-resteasy-client` and
`quarkus-resteasy-client-jsonb`.

The module configures the client URL and listens on port `8081`:

```properties
quarkus.http.port=8081
quarkus.rest-client.post-service.url=http://localhost:8080
quarkus.rest-client.post-service.scope=jakarta.inject.Singleton
```

The JSON-B client provider is supplied by the module's
`quarkus-resteasy-client-jsonb` extension. See [JSON-B on REST
clients](./jsonb-client.md).

## Run the client

Start the posts service on port `8080`, then run the client from
`restclient-classic`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The client application uses port
`8081`.

## What's next

- Compare Mutiny results in [MicroProfile REST Client with Quarkus REST](./microprofile-reactive.md).
- See [JAX-RS Client API](./jaxrs-client.md) for a programmatic alternative.
