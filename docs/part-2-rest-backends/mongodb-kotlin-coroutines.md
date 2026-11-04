# Reactive MongoDB Panache with Kotlin coroutines

The `mongodb-kotlin-co` module combines Quarkus REST, Kotlin coroutines, and
Reactive MongoDB Panache. Use it when resource methods should suspend while
reactive MongoDB operations complete.

## Sample structure

The POM includes `quarkus-mongodb-panache-kotlin`,
`quarkus-rest-kotlin`, and `quarkus-rest-jackson`.
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/mongodb-kotlin-co/src/main/kotlin/com/example/PostRepository.kt)
implements `ReactivePanacheMongoRepository<Post>`.
[`PostResources`](https://github.com/hantsy/quarkus-sandbox/blob/master/mongodb-kotlin-co/src/main/kotlin/com/example/PostResources.kt)
uses suspending endpoints and converts a reactive stream to a Kotlin Flow for
the list endpoint.

Configure MongoDB for your environment. The root Compose file defines a
MongoDB service on port `27017`.

## Run the sample

Start MongoDB from the repository root:

```shell
docker compose up -d mongodb
```

Then start the application from the `mongodb-kotlin-co` directory:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Run `./mvnw test` to execute the
module's tests.

## What's next

- Compare the blocking API in [MongoDB Panache with Kotlin](./mongodb-kotlin.md).
- See [Kotlin coroutines with Hibernate Reactive](./hibernate-reactive-kotlin.md).
