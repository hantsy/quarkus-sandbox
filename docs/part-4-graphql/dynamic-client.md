# Dynamic SmallRye GraphQL client

The `graphql-client` module uses SmallRye's dynamic GraphQL client to build a
query document in code. Use this API when you need to choose fields or
construct a query at runtime.

## Configure the client

The dynamic client uses config key `post-client-dynamic`:

```properties
quarkus.smallrye-graphql-client.post-client-dynamic.url=http://localhost:8080/graphql
```

[`PostDynamicClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/java/com/example/demo/PostDynamicClient.java)
injects `DynamicGraphQLClient`, builds a document using the SmallRye GraphQL
client core API, executes it synchronously, and maps the `allPosts` field to
Java objects.

## Run the client

Start the `graphql` server on port `8080`, then run the `graphql-client`
module:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. `Main` invokes this dynamic client
alongside the other clients.

## What's next

- See [Typesafe SmallRye GraphQL client](./typesafe-client.md).
- See [JAX-RS Client for GraphQL](./jaxrs-client.md).
