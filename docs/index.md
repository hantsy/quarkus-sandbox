# Quarkus Cookbook

This cookbook helps Java and Kotlin developers explore the Quarkus examples
in this repository. Follow the first two chapters to build a REST API, then
choose a persistence backend, client, GraphQL, messaging, or other recipe.
Each sample is a standalone Maven project; check its `pom.xml` and
configuration before running it.

## Part 1: Get started

Start with the Quarkus REST sample, then compare it with the Spring
programming model running on Quarkus:

- [Build a REST API with Quarkus](./01-start.md)
- [Build the API with Spring extensions](./02-spring.md)

## Part 2: Choose a REST backend

Compare relational databases, Hibernate ORM and Reactive, MongoDB, and
serialization customizations:

- [REST backends and data access](./rest-backends.md)
- [Customize JSON serialization](./serialization.md)

## Part 3: Call REST services

- [Call REST APIs from Quarkus](./rest-clients.md)

## Part 4: Build and call GraphQL APIs

- [Build a GraphQL API](./graphql.md)
- [Call GraphQL APIs](./graphql-client.md)

## Part 5: Send and receive messages

- [Send and receive messages](./messaging.md)

## Part 6: Explore other examples

- [Quartz, multipart, and legacy samples](./other-examples.md)
