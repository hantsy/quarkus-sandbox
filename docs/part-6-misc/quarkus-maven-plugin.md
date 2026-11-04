# Use the Quarkus Maven plugin

The Quarkus Maven plugin integrates Quarkus tasks with Maven. This recipe
shows how to run development mode, package a sample, and add an extension with
the wrapper in the `rest` module.

## Run development mode

From the repository root, change to the `rest` module:

```shell
cd rest
```

Start the application in development mode:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The sample starts on port `8080` and
uses PostgreSQL Dev Services, which requires a working container runtime.
Stop the process with `Ctrl+C`.

## Package the application

From the `rest` module, run Maven's `package` lifecycle:

```shell
./mvnw clean package
```

On Windows, run `mvnw.cmd clean package`. This runs the configured Maven
lifecycle, including the Quarkus build. Check the command output for build or
test failures.

## Add an extension

The original getting-started article demonstrates adding JSON-B, validation,
OpenAPI, persistence, and JDBC extensions through the Maven plugin. To add an
extension to a project, run the command from that project's directory:

```shell
./mvnw quarkus:add-extension -Dextensions=quarkus-smallrye-openapi
```

On Windows, run
`mvnw.cmd quarkus:add-extension -Dextensions=quarkus-smallrye-openapi`. This
updates the project's dependencies. The repository's `rest-classic` module
already has the OpenAPI extension; see [OpenAPI and Swagger UI](../part-2-rest-backends/openapi.md)
for its configuration and endpoints.

The archived [original getting-started article](../articles/01-start.md)
uses early Quarkus plugin versions and syntax. Use the commands on this page
for current sample workflows, and check the [Quarkus Maven plugin
guide](https://quarkus.io/guides/quarkus-maven-plugin) for other goals.

## What's next

- Compare the [Quarkus CLI](./quarkus-cli.md).
- Return to the [Part 6 index](./index.md).
