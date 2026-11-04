# Part 2: REST backends and data access

Choose a recipe by persistence API, database, JSON provider, or HTTP payload
type. The repository has Hibernate ORM and Hibernate Reactive examples,
reactive PostgreSQL clients, MongoDB, Jakarta Data, and Vert.x routing.

Each sample is an independent Maven module. Check its POM and
`application.properties` for the precise extensions and database setup; do
not assume datasource settings are shared between samples.

## Relational persistence

- [Hibernate Reactive with Panache](./hibernate-reactive-panache.md)
- [Hibernate Reactive with the session API](./hibernate-reactive-session.md)
- [Reactive PostgreSQL Client](./reactive-pg-client.md)
- [Hibernate ORM with Panache](./hibernate-orm-panache.md)
- [Kotlin coroutines with Hibernate Reactive](./hibernate-reactive-kotlin.md)
- [Jakarta Data repositories](./jakarta-data.md)
- [Vert.x Router with PostgreSQL](./vertx-router-postgres.md)

The Spring Data JPA sample is part of [Getting started](../part-1-getting-started/spring.md).

## MongoDB

- [MongoDB Panache with Kotlin](./mongodb-kotlin.md)
- [Reactive MongoDB Panache with Kotlin coroutines](./mongodb-kotlin-coroutines.md)

## JSON and request bodies

- [Customize JSON-B](./jsonb.md)
- [Customize Jackson](./jackson.md)
- [Receive and send multipart data](./multipart.md)

## What's next

- Compare [REST client implementations](../part-3-rest-clients/index.md).
- See [GraphQL data and APIs](../part-4-graphql/index.md).
