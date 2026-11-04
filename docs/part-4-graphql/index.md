# Part 4: GraphQL

The GraphQL examples separate the API server from its clients. The server
module exposes queries, mutations, computed fields, and a subscription. The
client module demonstrates typesafe, dynamic, and JAX-RS client APIs.

## Recipes

- [Build a GraphQL API](./server.md)
- [Typesafe GraphQL client](./typesafe-client.md)
- [Dynamic GraphQL client](./dynamic-client.md)
- [Java HTTP Client for GraphQL](./java-http-client.md)
- [JAX-RS Client for GraphQL](./jaxrs-client.md)

The client examples target `http://localhost:8080/graphql` by default. Start
the server before the client application. Its main class exercises all client
styles.

## What's next

- See [REST clients](../part-3-rest-clients/index.md) for HTTP client APIs.
- See [Messaging](../part-5-messaging/index.md) for broker-based communication.
