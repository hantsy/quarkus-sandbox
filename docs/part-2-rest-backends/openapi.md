# Document a REST API with OpenAPI and Swagger UI

The `rest-classic` module combines RESTEasy Classic with the
`quarkus-smallrye-openapi` extension. Its
[`PostResources`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-classic/src/main/java/com/example/web/PostResources.java)
uses MicroProfile OpenAPI annotations such as `@Operation`, `@Parameter`,
`@RequestBody`, and `@APIResponse` to describe endpoint behavior, inputs,
schemas, and response headers.

## Generated endpoints

When the `rest-classic` application is running in development mode, the
generated OpenAPI document is available at:

- `http://localhost:8080/q/openapi` for YAML.
- `http://localhost:8080/q/openapi?format=json` for JSON.
- `http://localhost:8080/q/swagger-ui` for Swagger UI.

Swagger UI is available by default in development and test modes. The
generated document includes JAX-RS endpoints and uses explicit OpenAPI
annotations where the sample provides them. For example, the create-post
operation documents its request body, `201` response, and `Location` header.

## Run and inspect the sample

From the `rest-classic` directory, start development mode:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Open
`http://localhost:8080/q/swagger-ui` or request the OpenAPI document with
`curl.exe http://localhost:8080/q/openapi`.

The module already includes `quarkus-smallrye-openapi` in its POM. Its
[`application.properties`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-classic/src/main/resources/application.properties)
contains commented examples for changing the OpenAPI and Swagger UI paths.
They are comments, so the default paths above are currently in effect.

## Include Swagger UI in production

Swagger UI is normally limited to development and test. To include it in a
production build, add this build-time setting to the module's
`application.properties`:

```properties
quarkus.swagger-ui.always-include=true
```

Only enable this when the UI is intended to be publicly available; the OpenAPI
document and interactive UI can expose API structure to users.

## What's next

- Compare the persistence layer in
  [Hibernate ORM with Panache](./hibernate-orm-panache.md).
- See the [REST client recipes](../part-3-rest-clients/index.md) for calling
  HTTP APIs from Quarkus.
