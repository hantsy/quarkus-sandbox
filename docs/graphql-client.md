# Call GraphQL APIs

The `graphql-client` module includes a typesafe SmallRye GraphQL client, a
dynamic client, and a programmatic JAX-RS client. Use the typesafe API when
you want GraphQL operations represented as Java interface methods; use the
dynamic API when you build a query at runtime.

## Configure the clients

The typesafe and dynamic clients use separate configuration keys. The sample
points both clients at the GraphQL server's `/graphql` endpoint:

```properties
quarkus.smallrye-graphql-client.post-client-typesafe.url=http://localhost:8080/graphql
quarkus.smallrye-graphql-client.post-client-dynamic.url=http://localhost:8080/graphql
```

These settings are from the module's
[`application.properties`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/resources/application.properties).

## Choose a client style

The
[`PostGraphQLClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/java/com/example/demo/PostGraphQLClient.java)
declares queries and mutations with `@GraphQLClientApi`. It also demonstrates
`ErrorOr<Post>` for a query that can return GraphQL errors.

The
[`PostDynamicClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/java/com/example/demo/PostDynamicClient.java)
constructs a GraphQL document with the SmallRye dynamic client API and selects
the fields to return.

The
[`JaxrsClient`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client/src/main/java/com/example/demo/JaxrsClient.java)
posts a JSON GraphQL document to `/graphql` using the JAX-RS Client API. This
example parses the JSON response into Java maps and model objects.

## Run a client against the sample server

1. Start the server from the `graphql` directory:

   ```shell
   ./mvnw quarkus:dev
   ```

2. In a second terminal, start the `graphql-client` module:

   ```shell
   cd graphql-client
   ./mvnw quarkus:dev
   ```

   On Windows, run `mvnw.cmd quarkus:dev` in each module.

The client configuration points to `http://localhost:8080/graphql`. Run only
one server on port `8080` while using these defaults.

## What's next

- See [Build a GraphQL API](./graphql.md) for the server operations.
- See [Call REST APIs from Quarkus](./rest-clients.md) for REST clients.
