# Build a GraphQL API

The `graphql` module builds a GraphQL API with SmallRye GraphQL. Use it to
explore queries, mutations, field resolvers, and subscriptions in a Quarkus
application. The sample uses an in-memory posts store; it does not persist data
to a database.

## Start the server

1. Change to the `graphql` module:

   ```shell
   cd graphql
   ```

2. Start the application in development mode:

   ```shell
   ./mvnw quarkus:dev
   ```

   On Windows, run `mvnw.cmd quarkus:dev`.

The GraphQL endpoint is `/graphql`. The sample's
[`GraphQLResource`](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql/src/main/java/com/example/demo/GraphQLResource.java)
declares the API operations:

| Operation | Purpose |
|---|---|
| `allPosts` query | Returns the posts in the in-memory store |
| `postById` query | Finds a post by its ID |
| `createPost` mutation | Adds a post to the in-memory store |
| `postCreated` subscription | Streams notifications when a post is created |

The `countOfComments` field is resolved from the `Post` source object. The
subscription returns a Mutiny `Multi`.

## Send a query

Send a GraphQL query to `http://localhost:8080/graphql`. For example:

```json
{
  "query": "{ allPosts { id title content countOfComments } }"
}
```

In PowerShell, save that JSON as a single-quoted string and post it with
`curl.exe`:

```powershell
curl.exe -X POST http://localhost:8080/graphql `
  -H "Content-Type: application/json" `
  --data-raw '{"query":"{ allPosts { id title content countOfComments } }"}'
```

The response contains the selected fields under `data.allPosts`. Select only
the fields the client needs.

## What's next

- See [Call GraphQL APIs](./graphql-client.md) for typesafe, dynamic, and
  JAX-RS client examples.
- See [REST clients](./rest-clients.md) for REST client options.
