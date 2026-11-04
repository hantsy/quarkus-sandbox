# Jakarta Data repositories

The `jakarta-data` module demonstrates Jakarta Data repository interfaces with
Hibernate ORM and PostgreSQL. Use it to explore the repository API represented
in the sample, rather than assuming it is the same as Panache or Spring Data.

## Sample structure

The POM includes Hibernate ORM, JDBC PostgreSQL, RESTEasy Classic with
Jackson, and the dependencies declared for Jakarta Data.
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/jakarta-data/src/main/java/com/example/repository/PostRepository.java)
and `CommentRepository` extend Jakarta Data repository interfaces. The
entities and resource classes show how the repository interfaces fit into
the application.

This sample has its own Jakarta Data and Hibernate configuration. Check its
POM and `application.properties` before choosing a compatible database or
changing the API dependencies.

## Run the sample

From the `jakarta-data` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Configure PostgreSQL or use its
development database setup as described by the module. Run `./mvnw test` to
execute the tests.

## What's next

- Compare with [Hibernate ORM and Panache](./hibernate-orm-panache.md).
- See [Spring Data JPA on Quarkus](../part-1-getting-started/spring.md).
