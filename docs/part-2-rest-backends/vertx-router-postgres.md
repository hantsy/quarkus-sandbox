# Vert.x Router with PostgreSQL

The `vertx-routes` module exposes an HTTP API with Vert.x Router and stores
posts with the reactive PostgreSQL client. Use it to explore Vert.x routing
without JAX-RS resource classes.

## Sample structure

The module depends on Quarkus reactive routes, the reactive PostgreSQL client,
and Jackson. `RoutesObserver` registers the routes, `PostsHandler` implements
the endpoints, and `PostRepository` executes SQL through the reactive client.

Configure PostgreSQL for this module before starting the application. Check
its `application.properties` and root Compose file for local database setup.

## Run the sample

From the `vertx-routes` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Run `./mvnw test` to execute tests.

## What's next

- Compare with [Reactive PostgreSQL Client and Quarkus REST](./reactive-pg-client.md).
- See [REST clients](../part-3-rest-clients/index.md) for calling REST APIs.
