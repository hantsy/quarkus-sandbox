# Reactive PostgreSQL Client

The `rest-pgclient` module accesses PostgreSQL with the reactive SQL client
and Mutiny. It maps rows to Java records directly instead of using an ORM.

## Sample structure

The POM includes `quarkus-reactive-pg-client`, `quarkus-rest`, and
`quarkus-rest-jsonb`. The
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-pgclient/src/main/java/com/example/PostRepository.java)
injects a Vert.x Mutiny `Pool`, issues prepared queries, maps rows, and returns
Mutiny `Uni` or `Multi` values. The resource uses those operations for
`/posts`.

Configure PostgreSQL according to the module's `application.properties`.
Unlike the Hibernate samples, the repository contains explicit SQL and row
mapping, so the database table must match those queries.

## Run the sample

From the `rest-pgclient` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Start or configure PostgreSQL before
calling the endpoints. Run `./mvnw test` to execute the tests.

## What's next

- Compare with [Hibernate Reactive and Panache](./hibernate-reactive-panache.md).
- See [Vert.x Router with PostgreSQL](./vertx-router-postgres.md) for a
  non-JAX-RS routing style.
