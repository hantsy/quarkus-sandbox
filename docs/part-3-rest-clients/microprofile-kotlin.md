# MicroProfile REST Client with Kotlin

The `restclient-kotlin` module declares a MicroProfile REST Client in Kotlin.
Its methods return JAX-RS `Response` values, allowing the caller to inspect
the response directly.

## Inspect the client interface

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-kotlin/src/main/kotlin/com/example/PostResourceClient.kt)
uses `@RegisterRestClient` and declares methods for posts and counts. Its POM
includes the RESTEasy Classic REST client with Jackson.

The module configures a posts service at port `8080`, a local HTTP port of
`8081`, and a singleton client scope in its `application.properties`.
The interface package determines the REST client config-key prefix.

## Run the client

Start a posts service at the configured URL, then run the client from
`restclient-kotlin`:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- See [the Kotlin coroutine client](./microprofile-kotlin-coroutines.md) for
  suspending interface methods.
- See [Jackson on REST clients](./jackson-client.md) for its JSON extension.
