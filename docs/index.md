# Quarkus Cookbook

The cookbook organizes the repository's Java and Kotlin samples around
individual technologies. Start with the REST guides, then choose a persistence
backend, REST client, GraphQL API, messaging connector, or miscellaneous
example.

Each recipe links to its sample module. Modules are independent Maven
projects, and their Quarkus and Java versions, extensions, and service
requirements can differ. Check the module's `pom.xml` and application
configuration before running it.

## Parts

- [Part 1: Getting started](./part-1-getting-started/index.md) builds a REST
  API with Quarkus REST and compares Spring APIs running on Quarkus.
- [Part 2: REST backends and data access](./part-2-rest-backends/index.md)
  covers relational and document databases, JSON serialization, and multipart
  requests.
- [Part 3: REST clients](./part-3-rest-clients/index.md) compares each client
  API in its own recipe.
- [Part 4: GraphQL](./part-4-graphql/index.md) covers the GraphQL server and
  clients.
- [Part 5: Messaging](./part-5-messaging/index.md) has a separate recipe for
  each broker or messaging API.
- [Part 6: Miscellaneous](./part-6-misc/index.md) covers Quartz and legacy
  samples.

## Run a recipe

Run each Maven command from the sample module directory. Most modules include
`mvnw` for Unix-like systems and `mvnw.cmd` for Windows. Start any required
database or broker using the sample's documented Compose file or Dev Services
configuration.
