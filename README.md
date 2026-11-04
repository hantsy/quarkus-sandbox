# Quarkus Cookbook

Standalone Quarkus sample projects for Java and Kotlin.

## Documentation

The [cookbook](./docs/index.md) is organized into six parts, with each part
and technology documented in its own Markdown page:

1. [Getting started](./docs/part-1-getting-started/index.md): Quarkus REST and
   Spring APIs on Quarkus.
2. [REST backends](./docs/part-2-rest-backends/index.md): relational and
   MongoDB persistence, JSON-B, Jackson, and multipart.
3. [REST clients](./docs/part-3-rest-clients/index.md): MicroProfile,
   JAX-RS, Java HTTP, and Vert.x clients.
4. [GraphQL](./docs/part-4-graphql/index.md): server and separate typesafe,
   dynamic, Java HTTP, and JAX-RS client recipes.
5. [Messaging](./docs/part-5-messaging/index.md): AMQP, RabbitMQ, JMS, Pulsar,
   and Kafka.
6. [Miscellaneous](./docs/part-6-misc/index.md): Quartz and legacy examples.

## Sample projects

Each directory with a `pom.xml` is a separate Maven project. Quarkus versions,
Java versions, dependencies, and external services can differ. Check the
module's POM, `src/main/resources/application.properties`, tests, and Compose
files before running it.

| Topic | Modules |
|---|---|
| REST and relational persistence | [`rest`](./rest), [`rest-classic`](./rest-classic), [`rest-hibernate`](./rest-hibernate), [`rest-pgclient`](./rest-pgclient), [`rest-kotlin-co`](./rest-kotlin-co), [`spring`](./spring), [`jakarta-data`](./jakarta-data), [`vertx-routes`](./vertx-routes) |
| MongoDB | [`mongodb-kotlin`](./mongodb-kotlin), [`mongodb-kotlin-co`](./mongodb-kotlin-co) |
| REST clients | [`restclient-classic`](./restclient-classic), [`restclient-reactive`](./restclient-reactive), [`restclient-jaxrs`](./restclient-jaxrs), [`restclient-java11`](./restclient-java11), [`restclient-vertx`](./restclient-vertx), [`restclient-kotlin`](./restclient-kotlin), [`restclient-kotlin-co`](./restclient-kotlin-co) |
| GraphQL | [`graphql`](./graphql), [`graphql-client`](./graphql-client) |
| Messaging | [`amqp`](./amqp), [`amqp-rabbitmq`](./amqp-rabbitmq), [`rabbitmq`](./rabbitmq), [`jms`](./jms), [`pulsar`](./pulsar), [`kafka`](./kafka) |
| Other | [`multipart`](./multipart), [`quartz`](./quartz) |

The [`legacy`](./legacy) directory preserves older samples. The root
[`docker-compose.yml`](./docker-compose.yml) and module-level Compose files
define local services for some examples.
