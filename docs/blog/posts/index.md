# Original blog posts

These are the original long-form articles that accompanied examples in this
repository. They are preserved as historical references and are not rewritten
as the current cookbook. Their images are kept alongside the posts so the
original relative image links continue to work.

## Archive

- [Getting started with Quarkus](./01-start.md)
- [Spring APIs on Quarkus](./02-spring.md)
- [REST clients](./restclient.md)
- [Reactive APIs](./reactive.md)
- [GraphQL server](./graphql.md)
- [GraphQL clients](./graphql-client.md)
- [Jakarta Data](./jakarta-data.md)

## Topics not fully covered by the cookbook

The cookbook reorganizes the examples by technology, so it does not reproduce
every historical walkthrough. The following original material is still
missing or only summarized:

| Original article | Covered in the cookbook | Missing or abbreviated material |
|---|---|---|
| [Getting started](./01-start.md) | [Quarkus REST](../../part-1-getting-started/quarkus-rest.md), [REST backends](../../part-2-rest-backends/index.md), and [OpenAPI](../../part-2-rest-backends/openapi.md) now cover the API, persistence choices, and generated API docs. | The original's complete step-by-step project setup, full Bean Validation and exception-handling walkthrough, detailed test setup, and native/container build stages are not reproduced end-to-end. |
| [Spring APIs](./02-spring.md) | [Spring API on Quarkus](../../part-1-getting-started/spring.md) now describes the API, custom keyword search, and exception handling. | The original's project initialization and detailed datasource/JPA setup sequence are condensed into a sample-oriented guide. |
| [REST clients](./restclient.md) | [REST client recipes](../../part-3-rest-clients/index.md) cover MicroProfile REST Client, JAX-RS Client, and Java HTTP Client separately. | Historical API background and the longer exception-mapper walkthrough are abbreviated. |
| [Reactive APIs](./reactive.md) | Current reactive persistence and client examples are documented in [REST backends](../../part-2-rest-backends/index.md) and [REST clients](../../part-3-rest-clients/index.md). | The older CompletionStage and RxJava approaches are retained as historical code, not maintained cookbook recipes. |
| [GraphQL server](./graphql.md) | [GraphQL server](../../part-4-graphql/server.md) covers queries, mutations, field resolution, subscriptions, validation, and error configuration. | The original's longer GraphQL introduction and detailed walkthrough of exception handling and validation are condensed. |
| [GraphQL clients](./graphql-client.md) | [GraphQL client recipes](../../part-4-graphql/index.md) separate typesafe, dynamic, Java HTTP, and JAX-RS approaches. | The original's side-by-side client explanation and full exception-handling discussion are abbreviated. |
| [Jakarta Data](./jakarta-data.md) | [Jakarta Data repositories](../../part-2-rest-backends/jakarta-data.md) now describes the sample's repository styles and annotation processor. | The original's chronological setup/dependency walkthrough and broader feature explanation are not reproduced as a full Jakarta Data tutorial. |

Use the cookbook for the current organization and the archived posts when
you need the original narrative, screenshots, or historical context.
