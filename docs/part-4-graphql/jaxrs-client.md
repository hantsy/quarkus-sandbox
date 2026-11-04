# JAX-RS Client for GraphQL

The `graphql-client` module includes a programmatic JAX-RS Client API caller.
It posts a GraphQL request as JSON and maps the response envelope to Java
objects.

## Inspect the implementation

[`JaxrsClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/java/com/example/demo/JaxrsClient.java)
creates a client with `ClientBuilder`, posts a GraphQL document to
`http://localhost:8080/graphql`, and returns a `CompletionStage` of posts.
The sample's `Main` invokes this client along with the SmallRye and Java HTTP
clients.

## Run the client

Start the GraphQL server on port `8080`, then run the `graphql-client` module:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- See [Java HTTP Client for GraphQL](./java-http-client.md).
- See [JAX-RS Client API for REST](../part-3-rest-clients/jaxrs-client.md).
