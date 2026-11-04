# Schedule tasks with Quartz

The `quartz` module demonstrates persistent Quartz scheduling with a JDBC job
store, Hibernate ORM, Flyway, and PostgreSQL.

## Inspect the sample

The module's `application.properties` configures `quarkus.quartz.store-type`
as `jdbc_cmt`, disables clustering, and enables Flyway migration at startup.
The production profile configures PostgreSQL at `localhost` with the
`quarkus_test` database. Hibernate schema generation is disabled.

[`TaskResource`](https://github.com/hantsy/quarkus-sandbox/blob/master/quartz/src/main/java/com/example/demo/TaskResource.java)
exposes the task HTTP API, and `TaskBean` implements the scheduled work.
Review the module's Flyway migrations and Compose file before using a local
database.

## Run the sample

Start PostgreSQL according to the module's Compose and application
configuration. From the `quartz` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Run `./mvnw test` to execute tests.

## What's next

- Browse [other cookbook examples](./index.md).
- See [REST backends](../part-2-rest-backends/index.md) for persistence APIs.
