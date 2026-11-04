# Call REST APIs from Quarkus

Choose a client sample based on the API you want to use: MicroProfile REST
Client, JAX-RS Client, Java's HTTP Client, or Vert.x Web Client. These
standalone projects call a posts service; they do not all use the same JSON
provider or return type.

## Compare the client samples

| Sample | Client API | Return style |
|---|---|---|
| [`restclient-classic`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-classic) | MicroProfile REST Client with RESTEasy Classic | `CompletionStage` |
| [`restclient-reactive`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-reactive) | MicroProfile REST Client with Quarkus REST | Mutiny `Uni`; also demonstrates an event stream |
| [`restclient-kotlin`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-kotlin) | MicroProfile REST Client in Kotlin | Kotlin interface methods |
| [`restclient-kotlin-co`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-kotlin-co) | MicroProfile REST Client in Kotlin | Suspending coroutine methods |
| [`restclient-jaxrs`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-jaxrs) | Programmatic JAX-RS Client | `CompletionStage` |
| [`restclient-java11`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-java11) | `java.net.http.HttpClient` | `CompletionStage` |
| [`restclient-vertx`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-vertx) | Vert.x Web Client with Mutiny | `Uni` and `Multi` |

The `restclient-classic` and `restclient-reactive` modules both use a
declarative interface, but use different REST stacks and asynchronous return
types. The JAX-RS, Java HTTP, and Vert.x examples create or configure their
clients in code.

## Configure a MicroProfile REST Client

The declarative modules register an interface with `@RegisterRestClient`.
For example, the `restclient-reactive` module sets its HTTP port to `8081` and
configures its posts-service URL in
[`application.properties`](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-reactive/src/main/resources/application.properties):

```properties
quarkus.http.port=8081
com.example.demo.PostResourceClient/mp-rest/url=http://localhost:8080
com.example.demo.PostResourceClient/mp-rest/scope=jakarta.inject.Singleton
```

The client listens on port `8081`; the service URL points to port `8080`. Use
the configuration key for the specific client interface in the module you
run. The Kotlin modules use their own interface names and configuration keys.

The reactive client returns Mutiny `Uni` values for ordinary responses and
uses `Multi` for its server-sent event example. The classic client returns
`CompletionStage` values. Both register a response exception mapper to handle
error responses.

## Run a client and service

Start a posts service, such as the `rest` module, then start a client module in
a second terminal. From the `restclient-reactive` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The service and client must use the
ports configured by their respective `application.properties` files.

Check each client's tests and controller for its sample request path. The
[REST backend chapter](./rest-backends.md) lists the available service
implementations.

## What's next

- See [Customize JSON serialization](./serialization.md) for the JSON
  extensions used by the clients.
- See [GraphQL clients](./graphql-client.md) for clients that query GraphQL
  services.
