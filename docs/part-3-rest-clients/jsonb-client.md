# JSON-B on REST clients

JSON-B is used by the classic REST client and the JDK HTTP client examples.
Choose this page when you need the client-side serialization configuration;
server-side JSON-B customization is covered in
[Part 2](../part-2-rest-backends/jsonb.md).

## RESTEasy Classic client

The `restclient-classic` POM includes `quarkus-resteasy-client-jsonb`. Its
declarative MicroProfile client maps JSON responses to `Post` objects using
the configured JSON-B provider.

## JDK HTTP client

The `restclient-java11` module injects `Jsonb` and uses it to parse response
body strings. For a list response, it uses `jakarta.enterprise.util.TypeLiteral`
to preserve the generic list type.

See the client implementations in
[`restclient-classic`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-classic)
and
[`restclient-java11`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-java11).

## What's next

- See [JSON-B on REST APIs](../part-2-rest-backends/jsonb.md) for server-side
  customization.
- See [Jackson on REST clients](./jackson-client.md).
