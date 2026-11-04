# Hibernate Reactive with Panache

Use the `rest` sample to build Quarkus REST endpoints on Hibernate Reactive
with Panache. This module combines a Panache repository, Mutiny types, and a
reactive PostgreSQL client.

## Sample structure

The module's POM includes `quarkus-hibernate-reactive`,
`quarkus-reactive-pg-client`, and `quarkus-hibernate-reactive-panache`.
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/PostRepository.java)
implements `PanacheRepositoryBase<Post, UUID>`. Its query methods return
Mutiny `Uni` values, and
[`PostResources`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/PostResources.java)
maps them to HTTP responses.

The active datasource kind is PostgreSQL. The sample does not set a URL;
configure one or let Dev Services start a database. Hibernate schema
generation uses `drop-and-create`, so use a disposable database.

## Run the sample

From the `rest` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The API uses `/posts`. The
[getting-started recipe](../part-1-getting-started/quarkus-rest.md) shows a
create request and test command.

## What's next

- Compare the direct session API in [Hibernate Reactive with the session
  API](./hibernate-reactive-session.md).
- See [Reactive PostgreSQL Client](./reactive-pg-client.md) for SQL without an
  ORM.
