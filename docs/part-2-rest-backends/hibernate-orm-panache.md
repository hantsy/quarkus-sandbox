# Hibernate ORM with Panache

The `rest-classic` module combines RESTEasy Classic with Hibernate ORM and
Panache. Use it to compare imperative JPA persistence and classic JAX-RS
resources with the reactive REST samples.

## Sample structure

The module's POM includes `quarkus-resteasy`, `quarkus-resteasy-jsonb`,
`quarkus-hibernate-orm-panache`, and `quarkus-jdbc-postgresql`.
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-classic/src/main/java/com/example/repository/PostRepository.java)
and `CommentRepository` use Panache repository APIs;
[`PostService`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-classic/src/main/java/com/example/service/PostService.java)
contains application operations, and JAX-RS resources expose the HTTP API.

The module supports PostgreSQL for production configuration and uses H2 in its
test profile. Inspect `application.properties` and
`src/test/resources` before choosing a database.

## Run the sample

From the `rest-classic` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Run `./mvnw test` to use the module's
tests and test configuration.

## What's next

- Compare with [Hibernate Reactive and Panache](./hibernate-reactive-panache.md).
- See [JSON-B customization](./jsonb.md) for this module's customizer.
