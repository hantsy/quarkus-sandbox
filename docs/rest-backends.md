# REST backends and data access

Choose a sample by the persistence API or database you want to use. This
chapter covers the SQL, MongoDB, and Vert.x backend examples in the repository.
It does not compare the features or performance of the underlying databases.

Each row links to a standalone Maven project. Check its `pom.xml` and
`src/main/resources/application.properties` before you run it; connection
settings and Quarkus versions can differ between modules.

## Relational databases

The repository includes Hibernate ORM, Hibernate Reactive, direct reactive SQL,
Jakarta Data, and Vert.x database examples.

| Sample | REST layer | Data access | Start here for |
|---|---|---|---|
| [`rest`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest) | Quarkus REST | Hibernate Reactive with Panache and the reactive PostgreSQL client | Mutiny-based resource and repository APIs |
| [`rest-hibernate`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-hibernate) | Quarkus REST | Hibernate Reactive `Mutiny.SessionFactory` | Hibernate Reactive sessions and criteria queries |
| [`rest-pgclient`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-pgclient) | Quarkus REST | Reactive PostgreSQL Client with SQL and prepared queries | Database access without an ORM |
| [`rest-classic`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-classic) | RESTEasy Classic | Hibernate ORM with Panache and JDBC PostgreSQL | Imperative JPA-style persistence |
| [`rest-kotlin-co`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-kotlin-co) | Quarkus REST with Kotlin | Hibernate Reactive with Panache | Coroutine endpoints backed by a relational database |
| [`spring`](https://github.com/hantsy/quarkus-sandbox/tree/master/spring) | Spring Web on Quarkus | Spring Data JPA and JDBC PostgreSQL | Spring repository and controller APIs; see [the getting-started guide](./02-spring.md) |
| [`jakarta-data`](https://github.com/hantsy/quarkus-sandbox/tree/master/jakarta-data) | RESTEasy Classic | Jakarta Data repositories over Hibernate ORM | `CrudRepository`-style repository interfaces |
| [`vertx-routes`](https://github.com/hantsy/quarkus-sandbox/tree/master/vertx-routes) | Vert.x Router | Reactive PostgreSQL Client | Vert.x routes and reactive SQL |

The first two rows use Hibernate Reactive; they show different repository
styles. The
[`rest/PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/PostRepository.java)
uses the Panache repository API, while
[`rest-hibernate/PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-hibernate/src/main/java/com/example/PostRepository.java)
works with a `Mutiny.SessionFactory`.

The
[`rest-pgclient/PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-pgclient/src/main/java/com/example/PostRepository.java)
shows prepared statements and explicit row mapping. For imperative
Hibernate ORM and Panache, compare
[`rest-classic`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-classic).

## MongoDB

The two Kotlin MongoDB modules use MongoDB with Panache:

| Sample | REST style | Data access |
|---|---|---|
| [`mongodb-kotlin`](https://github.com/hantsy/quarkus-sandbox/tree/master/mongodb-kotlin) | RESTEasy Classic | `PanacheMongoRepository` |
| [`mongodb-kotlin-co`](https://github.com/hantsy/quarkus-sandbox/tree/master/mongodb-kotlin-co) | Quarkus REST with Kotlin coroutines | `ReactivePanacheMongoRepository` |

The first module uses the blocking repository API. The second uses the
reactive repository API and suspending resource methods. See
[`mongodb-kotlin/PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/mongodb-kotlin/src/main/kotlin/com/example/PostRepository.kt)
and
[`mongodb-kotlin-co/PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/mongodb-kotlin-co/src/main/kotlin/com/example/PostRepository.kt).

## JSON serialization

REST backends use different JSON extensions. Pick the provider used by the
module, then follow its customizer example:

- The `rest` module uses Jackson through `quarkus-rest-jackson`. Its
  [`CustomObjectMapper`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/CustomObjectMapper.java)
  customizes the `JsonMapper` date and time features.
- The `rest-classic` module uses JSON-B through
  `quarkus-resteasy-jsonb`. Its
  [`CustomJsonbConfig`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-classic/src/main/java/com/example/config/CustomJsonbConfig.java)
  implements `JsonbConfigCustomizer` to control field visibility, formatting,
  null values, and encoding.
- The `rest-pgclient` module uses JSON-B through `quarkus-rest-jsonb` without
  a custom JSON-B configuration.
- The `rest-hibernate` module uses Jackson through `quarkus-rest-jackson`.

For the distinct Jackson and JSON-B approaches, see [Customize JSON
serialization](./serialization.md).

## Run a backend sample

Each sample has its own Maven wrapper. For example, to start the `rest-pgclient`
module in development mode, run the following command from its directory:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Start or configure the database
described by that module before sending requests. Do not copy connection
settings from another module without checking its datasource kind and
configuration.

## What's next

- See [Customize JSON serialization](./serialization.md) for JSON-B and
  Jackson customization recipes.
- See [REST clients](./rest-clients.md) to call a REST backend from another
  Quarkus application.
