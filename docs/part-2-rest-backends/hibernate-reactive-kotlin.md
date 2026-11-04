# Kotlin coroutines with Hibernate Reactive

The `rest-kotlin-co` module implements the posts API with Kotlin coroutine
resource methods, Hibernate Reactive, and Panache. Use it to see suspending
functions call reactive repository operations.

## Sample structure

The POM includes Kotlin support, Quarkus REST, Hibernate Reactive, the
reactive PostgreSQL client, and Hibernate Reactive Panache for Kotlin.
[`PostResources`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-kotlin-co/src/main/kotlin/com/example/demo/PostResources.kt)
declares suspending HTTP methods and calls repository operations with
`awaitSuspending`. The repository uses Hibernate Reactive Panache.

Check `application.properties` for PostgreSQL connection configuration and
use a disposable database for this sample.

## Run the sample

From the `rest-kotlin-co` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Run `./mvnw test` to execute the tests.

## What's next

- Compare Kotlin client coroutines in
  [MicroProfile REST Client with Kotlin coroutines](../part-3-rest-clients/microprofile-kotlin-coroutines.md).
- See [MongoDB Panache with Kotlin coroutines](./mongodb-kotlin-coroutines.md).
