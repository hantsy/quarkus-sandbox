# Quarkus Sandbox

A personal sandbox project for experiencing the new features of the
[Quarkus framework](https://quarkus.io).

## Documentation

The [Quarkus Cookbook](https://hantsy.github.io/quarkus-sandbox/) documents
these samples as step-by-step recipes, organized into six parts: getting
started, REST backends and data access, REST clients, GraphQL, messaging, and
miscellaneous. The Markdown sources are in [`docs`](./docs).

## Sample projects

Each directory with a `pom.xml` is an independent Maven project. Quarkus
versions, Java versions, extensions, and external services differ between
them, so check the module's `pom.xml` and
`src/main/resources/application.properties` before you run it. The following
table lists each sample and the key extensions it declares, with test-scoped
dependencies omitted:

| Name | Description | Key extensions |
|:---|:---|:---|
| [`rest`](./rest) | Posts API with Quarkus REST, Hibernate Reactive, and Panache entities. | `quarkus-rest`, `quarkus-rest-jackson`, `quarkus-hibernate-reactive`, `quarkus-hibernate-reactive-panache`, `quarkus-reactive-pg-client`, `quarkus-hibernate-validator` |
| [`rest-hibernate`](./rest-hibernate) | Quarkus REST with the Hibernate Reactive session API. | `quarkus-rest`, `quarkus-rest-jackson`, `quarkus-hibernate-reactive`, `quarkus-reactive-pg-client`, `quarkus-hibernate-validator` |
| [`rest-classic`](./rest-classic) | RESTEasy Classic with Hibernate ORM with Panache, JSON-B, and OpenAPI. | `quarkus-resteasy`, `quarkus-resteasy-jsonb`, `quarkus-hibernate-orm-panache`, `quarkus-smallrye-openapi`, `quarkus-jdbc-postgresql` |
| [`rest-pgclient`](./rest-pgclient) | Quarkus REST on the Reactive PostgreSQL Client. | `quarkus-rest`, `quarkus-rest-jsonb`, `quarkus-reactive-pg-client`, `quarkus-hibernate-validator` |
| [`rest-kotlin-co`](./rest-kotlin-co) | Kotlin coroutines with Quarkus REST and Hibernate Reactive with Panache. | `quarkus-rest-kotlin`, `quarkus-kotlin`, `quarkus-hibernate-reactive-panache-kotlin`, `quarkus-reactive-pg-client` |
| [`vertx-routes`](./vertx-routes) | CRUD endpoints on a Vert.x `Router` with the Reactive PostgreSQL Client. | `quarkus-reactive-routes`, `quarkus-reactive-pg-client`, `quarkus-jackson` |
| [`spring`](./spring) | Spring Web, Spring DI, and Spring Data JPA APIs on Quarkus. | `quarkus-spring-web`, `quarkus-spring-di`, `quarkus-spring-data-jpa`, `quarkus-spring-boot-properties`, `quarkus-undertow`, `quarkus-jdbc-postgresql` |
| [`jakarta-data`](./jakarta-data) | Jakarta Data repository interfaces with Hibernate ORM. | `quarkus-hibernate-orm`, `jakarta.data-api`, `quarkus-resteasy-jackson`, `quarkus-smallrye-openapi`, `quarkus-jdbc-postgresql` |
| [`mongodb-kotlin`](./mongodb-kotlin) | MongoDB Panache with Kotlin and RESTEasy Classic. | `quarkus-mongodb-client`, `quarkus-mongodb-panache-kotlin`, `quarkus-resteasy-jackson` |
| [`mongodb-kotlin-co`](./mongodb-kotlin-co) | Reactive MongoDB Panache with Kotlin coroutines. | `quarkus-mongodb-panache-kotlin`, `quarkus-rest-kotlin`, `quarkus-kotlin`, `quarkus-rest-jackson` |
| [`restclient-classic`](./restclient-classic) | MicroProfile REST Client on RESTEasy Classic with JSON-B. | `quarkus-resteasy-client`, `quarkus-resteasy-client-jsonb`, `quarkus-resteasy-jsonb` |
| [`restclient`](./restclient) | MicroProfile REST Client on Quarkus REST with Jackson. | `quarkus-rest-client`, `quarkus-rest-client-jackson`, `quarkus-rest-jackson` |
| [`restclient-classic-kotlin`](./restclient-classic-kotlin) | MicroProfile REST Client in Kotlin on RESTEasy Classic. | `quarkus-resteasy-client`, `quarkus-kotlin`, `quarkus-resteasy-client-jackson` |
| [`restclient-kotlin-co`](./restclient-kotlin-co) | Typesafe REST Client with Kotlin coroutines on Quarkus REST. | `quarkus-rest-client`, `quarkus-kotlin`, `quarkus-rest-client-jackson` |
| [`restclient-classic-jaxrs`](./restclient-classic-jaxrs) | JAX-RS Client API with Jackson. | `quarkus-resteasy-client`, `quarkus-resteasy-client-jackson`, `quarkus-resteasy-jackson` |
| [`restclient-classic-java11`](./restclient-classic-java11) | The `java.net.http.HttpClient` API against a RESTEasy Classic endpoint. | `quarkus-resteasy`, `quarkus-resteasy-jsonb` |
| [`restclient-classic-vertx`](./restclient-classic-vertx) | The Vert.x Web Client with Mutiny types. | `quarkus-vertx`, `quarkus-resteasy-mutiny`, `quarkus-resteasy-jackson` |
| [`graphql`](./graphql) | GraphQL API with SmallRye GraphQL. | `quarkus-smallrye-graphql`, `quarkus-hibernate-validator` |
| [`graphql-client`](./graphql-client) | Typesafe, dynamic, Java HTTP, and JAX-RS GraphQL clients. | `quarkus-smallrye-graphql-client`, `quarkus-rest-client-jsonb` |
| [`amqp`](./amqp) | Reactive Messaging with the AMQP 1.0 connector. | `quarkus-messaging-amqp`, `quarkus-rest-jsonb` |
| [`amqp-rabbitmq`](./amqp-rabbitmq) | The AMQP 1.0 connector pointed at RabbitMQ. | `quarkus-messaging-amqp`, `quarkus-rest-jsonb` |
| [`rabbitmq`](./rabbitmq) | Reactive Messaging with the RabbitMQ connector. | `quarkus-messaging-rabbitmq`, `quarkus-rest-jsonb` |
| [`jms`](./jms) | Jakarta Messaging with the Qpid JMS client. | `quarkus-qpid-jms`, `quarkus-scheduler`, `quarkus-rest-jsonb` |
| [`pulsar`](./pulsar) | Reactive Messaging with the Pulsar connector. | `quarkus-messaging-pulsar`, `quarkus-rest-jsonb` |
| [`kafka`](./kafka) | Reactive Messaging with the Kafka connector. | `quarkus-messaging-kafka`, `quarkus-rest-jackson` |
| [`multipart`](./multipart) | Multipart uploads and downloads with Quarkus REST. | `quarkus-rest`, `quarkus-rest-client`, `quarkus-rest-jackson` |
| [`quartz`](./quartz) | Cron jobs with Quartz, Hibernate ORM with Panache, and Flyway migrations. | `quarkus-quartz`, `quarkus-hibernate-orm-panache`, `quarkus-flyway`, `quarkus-jdbc-postgresql` |

## Run a sample

Run each Maven command from the module directory. Every module ships `mvnw` for
Unix-like systems and `mvnw.cmd` for Windows. To start a module in development
mode, run the following command, and run `mvnw.cmd quarkus:dev` on Windows:

```bash
cd rest
./mvnw quarkus:dev
```

Some samples override `quarkus.http.port`: every `restclient-*` module listens
on port 8081 and points its client at a server on port 8080. Check the module's
`application.properties` for the port. Start any required database or broker
with Dev Services or a Compose file: the root
[`docker-compose.yml`](./docker-compose.yml) and
[`quartz/docker-compose.yml`](./quartz/docker-compose.yml) define local
services.

## Legacy samples

The [`legacy`](./legacy) directory preserves earlier examples that depend on
Java 8, RxJava 2, and older Vert.x and Hibernate Reactive APIs. Those projects
are kept for historical comparison, are not upgraded with the rest of the
repository, and are not covered by the recipes above. See
[Browse legacy samples](./docs/part-6-misc/legacy.md) in the cookbook for their
background and dependencies.
