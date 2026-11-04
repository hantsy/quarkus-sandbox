# MicroProfile REST Client with Kotlin coroutines

The `restclient-kotlin-co` module declares a MicroProfile REST Client using
Kotlin `suspend` methods. Use it to compare coroutine-based clients with
`CompletionStage`, `Uni`, and ordinary response types.

## Inspect and configure the client

[`PostResourceClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-kotlin-co/src/main/kotlin/com/example/demo/PostResourceClient.kt)
declares suspending methods that return a list of posts and a count. The POM
includes Quarkus REST, REST Client, Jackson, and Kotlin support.

The module sets port `8081` and points the client at `http://localhost:8080`.
Its configuration uses the interface's fully qualified class name as the
client key.

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
