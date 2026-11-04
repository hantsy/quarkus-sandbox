# MongoDB Panache with Kotlin

The `mongodb-kotlin` module uses the blocking MongoDB Panache repository API
with Kotlin and RESTEasy Classic.

## Sample structure

The POM includes `quarkus-mongodb-client`,
`quarkus-mongodb-panache-kotlin`, RESTEasy Classic, and Jackson.
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/mongodb-kotlin/src/main/kotlin/com/example/PostRepository.kt)
implements `PanacheMongoRepository<Post>` and includes a title lookup.
`PostResources` exposes the repository operations.

Configure the MongoDB connection for your environment. The root
`docker-compose.yml` includes a MongoDB service mapped to port `27017`.

## Run the sample

Start MongoDB using the repository's root Compose file, then start the
application from the `mongodb-kotlin` directory:

```shell
docker compose up -d mongodb
```

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Run `./mvnw test` to execute the
module's tests.

## What's next

- See [Reactive MongoDB Panache with Kotlin coroutines](./mongodb-kotlin-coroutines.md).
- Compare relational backends in [the part index](./index.md#relational-persistence).
