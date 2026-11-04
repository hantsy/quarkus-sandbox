# Quarkus Cookbook

A collection of standalone Quarkus sample projects for Java and Kotlin.

## Documentation

The [cookbook](./docs/index.md) is organized by the tasks you can try:

1. [Get started with a REST API](./docs/01-start.md) and its
   [Spring alternative](./docs/02-spring.md).
2. Compare [REST backends and data access](./docs/rest-backends.md), including
   [JSON serialization](./docs/serialization.md).
3. Explore [REST clients](./docs/rest-clients.md).
4. Build and call [GraphQL APIs](./docs/graphql.md) with the
   [GraphQL client samples](./docs/graphql-client.md).
5. Connect to [messaging brokers](./docs/messaging.md).
6. Try [Quartz and multipart examples](./docs/other-examples.md).

## Sample projects

Each directory with a `pom.xml` is a separate Maven project. The samples can
use different Quarkus versions, Java versions, extensions, and local services.
Read that module's `pom.xml`, `src/main/resources/application.properties`,
tests, and Compose files before running it.

| Topic | Modules |
|---|---|
| REST and relational persistence | [`rest`](./rest), [`rest-classic`](./rest-classic), [`rest-hibernate`](./rest-hibernate), [`rest-pgclient`](./rest-pgclient), [`rest-kotlin-co`](./rest-kotlin-co), [`spring`](./spring), [`jakarta-data`](./jakarta-data), [`vertx-routes`](./vertx-routes) |
| MongoDB | [`mongodb-kotlin`](./mongodb-kotlin), [`mongodb-kotlin-co`](./mongodb-kotlin-co) |
| REST clients | [`restclient-classic`](./restclient-classic), [`restclient-reactive`](./restclient-reactive), [`restclient-jaxrs`](./restclient-jaxrs), [`restclient-java11`](./restclient-java11), [`restclient-vertx`](./restclient-vertx), [`restclient-kotlin`](./restclient-kotlin), [`restclient-kotlin-co`](./restclient-kotlin-co) |
| GraphQL | [`graphql`](./graphql), [`graphql-client`](./graphql-client) |
| Messaging | [`amqp`](./amqp), [`amqp-rabbitmq`](./amqp-rabbitmq), [`rabbitmq`](./rabbitmq), [`jms`](./jms), [`pulsar`](./pulsar), [`kafka`](./kafka) |
| Other | [`multipart`](./multipart), [`quartz`](./quartz) |

The [`legacy`](./legacy) directory contains earlier examples for reference.
The root [`docker-compose.yml`](./docker-compose.yml) and module-level Compose
files define some of the local services used by the samples.
