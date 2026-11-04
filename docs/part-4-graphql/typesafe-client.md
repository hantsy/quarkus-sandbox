# Typesafe SmallRye GraphQL client

The `graphql-client` module declares a typesafe GraphQL API with
`@GraphQLClientApi`. Use this client when you want GraphQL operations and
response types represented as Java methods.

## Configure the client

The `PostGraphQLClient` uses config key `post-client-typesafe`. The module
points it at `http://localhost:8080/graphql`:

```properties
quarkus.smallrye-graphql-client.post-client-typesafe.url=http://localhost:8080/graphql
```

The
[`PostGraphQLClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/java/com/example/demo/PostGraphQLClient.java)
declares list and summary queries, a query that returns `ErrorOr<Post>`, and a
mutation. The client interface uses the GraphQL API types from the
`graphql-client` module.

`ErrorOr<Post>` is the sample's explicit way to represent a GraphQL operation
that may return an error alongside its data. Inspect the result before using
the post value; a GraphQL response can contain both `data` and `errors`, and
an HTTP-success status alone does not mean every requested field succeeded.

## Run the client

Start the `graphql` server on port `8080`, then run the
`graphql-client` module:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The module's `Main` class calls the
typesafe client along with the other client examples.

## What's next

- See [Dynamic SmallRye GraphQL client](./dynamic-client.md).
- See the [GraphQL part index](./index.md) for all client styles.
