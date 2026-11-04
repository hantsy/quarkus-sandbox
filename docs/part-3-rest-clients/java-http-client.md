# Java HTTP Client

The `restclient-java11` module uses the JDK's `java.net.http.HttpClient` to
call the posts service. It sends asynchronous requests and maps response
bodies with JSON-B.

## Inspect the client

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-java11/src/main/java/com/example/demo/PostResourceClient.java)
builds an HTTP/2 client and calls the service with `sendAsync`. It parses
count responses as text and converts JSON response bodies to Java objects
with an injected `Jsonb` instance. The module includes RESTEasy Classic and
JSON-B, but the outbound calls use the JDK HTTP client.

The sample currently uses `http://localhost:8080` in its request URIs. The
`application.properties` sets the local Quarkus HTTP port to `8081`.

## Run the client

Start the posts service on port `8080`, then run the client from
`restclient-java11`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- See [JSON-B on REST clients](./jsonb-client.md) for the sample's decoder.
- Compare [JAX-RS Client API](./jaxrs-client.md).
