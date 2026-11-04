# Build a GraphQL API with SmallRye GraphQL

The `graphql` module exposes a GraphQL API with SmallRye GraphQL. It
demonstrates queries, a mutation, a computed field, and a subscription. Its
posts store is in memory and does not persist data to a database.

## Start the server

From the `graphql` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The GraphQL endpoint is
`http://localhost:8080/graphql`.

## Inspect the API

The module uses `quarkus-smallrye-graphql`. Its
[`GraphQLResource`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql/src/main/java/com/example/demo/GraphQLResource.java)
declares:

- `allPosts` and `postById` queries.
- A `createPost` mutation.
- A `countOfComments` field resolver that reads the source `Post`.
- A `postCreated` subscription that returns a Mutiny `Multi`.

The `PostService` stores posts in an in-memory list and publishes creation
events through a broadcast processor.

## Send a query

Send a GraphQL request to the endpoint. In PowerShell, run:

```powershell
curl.exe -X POST http://localhost:8080/graphql `
  -H "Content-Type: application/json" `
  --data-raw '{"query":"{ allPosts { id title content countOfComments } }"}'
```

The response contains the selected fields under `data.allPosts`.

## What's next

- See the [typesafe client](./typesafe-client.md), [dynamic client](./dynamic-client.md),
  [Java HTTP client](./java-http-client.md), and [JAX-RS client](./jaxrs-client.md).
- Return to the [GraphQL part index](./index.md).
