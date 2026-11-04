# Jackson on REST clients

Several declarative and programmatic REST clients use Jackson extensions.
This page covers client-side codecs; server-side Jackson customization is
described in [Part 2](../part-2-rest-backends/jackson.md).

## RESTEasy Classic and JAX-RS clients

The `restclient-kotlin` and `restclient-jaxrs` modules use
`quarkus-resteasy-client-jackson`. They pair the classic RESTEasy client with
Jackson serialization.

## Quarkus REST client

The `restclient-reactive` and `restclient-kotlin-co` modules use
`quarkus-rest-client-jackson`. The client extension supplies Jackson support
for entity responses.

## Check the selected extension

Use the extension matching the REST stack:

- `quarkus-resteasy-client-jackson` for RESTEasy Classic clients.
- `quarkus-rest-client-jackson` for Quarkus REST clients.

The
[`restclient-jaxrs` POM](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-jaxrs/pom.xml)
and
[`restclient-reactive` POM](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-reactive/pom.xml)
show these dependencies in context.

## What's next

- See [Jackson on REST APIs](../part-2-rest-backends/jackson.md) for a server
  customizer.
- See [JSON-B on REST clients](./jsonb-client.md).
