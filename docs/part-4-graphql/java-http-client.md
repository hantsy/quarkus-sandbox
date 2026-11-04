# Java HTTP Client for GraphQL

The `graphql-client` module includes a client built with the JDK
`java.net.http.HttpClient`. It sends a GraphQL document as JSON, reads the
response body, and extracts the `data.allPosts` array.

## Inspect the implementation

[`JvmClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/java/com/example/demo/JvmClient.java)
builds an HTTP/2 client and uses `sendAsync` to post to
`http://localhost:8080/graphql`. It parses the JSON envelope with Jakarta JSON
Processing, then maps the selected array to `Post` values with JSON-B.

The module's `Main` calls `JvmClient.getAllPosts()` along with the other
GraphQL clients.

## Run the client

Start the GraphQL server on port `8080`, then run the `graphql-client` module:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- See [JAX-RS Client for GraphQL](./jaxrs-client.md).
- See [Java HTTP Client for REST](../part-3-rest-clients/java-http-client.md).
