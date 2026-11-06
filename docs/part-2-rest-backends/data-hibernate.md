# Jakarta Data repositories

The `data-hibernate` module demonstrates Jakarta Data repository interfaces with
Hibernate ORM and PostgreSQL. Use it to explore the repository API represented
in the sample, rather than assuming it is the same as Panache or Spring Data.

## Sample structure

The POM declares `quarkus-data-hibernate`, the Quarkus Data extension built on
top of Hibernate ORM, which brings both Hibernate ORM and the Jakarta Data API,
plus JDBC PostgreSQL and RESTEasy Classic with Jackson.
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/data-hibernate/src/main/java/com/example/repository/PostRepository.java)
and `CommentRepository` extend Jakarta Data repository interfaces. The
entities and resource classes show how the repository interfaces fit into
the application.

The application-facing
[`Blogger`](https://github.com/hantsy/quarkus-sandbox/blob/master/data-hibernate/src/main/java/com/example/service/Blogger.java)
repository demonstrates more than basic CRUD: a JPQL `@Query` returns a
paginated projection, `@Find` methods derive lookups, and Jakarta Data
`@Insert`, `@Update`, and `@Delete` declare writes. It also shows explicit
query parameters, ordering, limits, and a default method that fetches a post's
comments. The Maven compiler configuration uses `quarkus-data-processor` as the
annotation processor; keep it when changing this sample's repository
declarations.

This sample has its own Jakarta Data and Hibernate configuration. Check its
POM and `application.properties` before choosing a compatible database or
changing the API dependencies.

## Run the sample

From the `data-hibernate` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Configure PostgreSQL or use its
development database setup as described by the module. Run `./mvnw test` to
execute the tests.

## What's next

- Compare with [Hibernate ORM and Panache](./hibernate-orm-panache.md).
- See [Spring Data JPA on Quarkus](../part-1-getting-started/spring.md).
