# Hibernate Reactive with the session API

The `rest-hibernate` module uses Hibernate Reactive's Mutiny session API
directly instead of Panache. Choose this sample when you want to write session
operations and criteria queries yourself.

## Sample structure

The module depends on `quarkus-hibernate-reactive`,
`quarkus-reactive-pg-client`, `quarkus-rest`, and `quarkus-rest-jackson`.
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-hibernate/src/main/java/com/example/PostRepository.java)
injects a `Mutiny.SessionFactory`; it performs reads with `withSession` and
writes with `withTransaction`.

The resource exposes the posts API with Mutiny `Uni` values. Check the module's
`application.properties` and Dev Services settings before starting PostgreSQL.

## Run the sample

From the `rest-hibernate` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Run `./mvnw test` to execute its tests.

## What's next

- Compare the repository abstraction in [Hibernate Reactive with
  Panache](./hibernate-reactive-panache.md).
- See [Hibernate ORM with Panache](./hibernate-orm-panache.md) for imperative
  persistence.
