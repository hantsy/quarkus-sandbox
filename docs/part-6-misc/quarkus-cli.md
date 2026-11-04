# Use the Quarkus CLI

Use the Quarkus CLI to run and build a Quarkus project from its module
directory, or to add an extension. This recipe uses the repository's `rest`
module as an example; it does not cover every CLI command.

## Before you begin

Install the Quarkus CLI and Java. See the [Quarkus CLI installation
guide](https://quarkus.io/guides/cli-tooling) for platform-specific setup.

## Run a sample in development mode

From the repository root, change to the `rest` module:

```shell
cd rest
```

Start development mode:

```shell
quarkus dev
```

The sample starts on port `8080`. Its PostgreSQL Dev Services configuration
requires a working container runtime. Stop the process with `Ctrl+C`.

## Build the sample

From the `rest` module, create a build:

```shell
quarkus build
```

The CLI detects the project's build tool and delegates to it. Check the build
output for errors before running the packaged application.

## Add an extension

To add an extension to a Quarkus project, run the command from that project's
directory. For example, to add SmallRye OpenAPI:

```shell
quarkus extension add quarkus-smallrye-openapi
```

This updates the project dependencies. The repository's `rest-classic` sample
already includes SmallRye OpenAPI, so you do not need to add it there. See
[OpenAPI and Swagger UI](../part-2-rest-backends/openapi.md) for that sample.

For more CLI commands, see the [Quarkus CLI guide](https://quarkus.io/guides/cli-tooling).

## What's next

- See [Quarkus Maven plugin usage](./quarkus-maven-plugin.md) for equivalent
  commands using each module's Maven wrapper.
- Return to the [Part 6 index](./index.md).
