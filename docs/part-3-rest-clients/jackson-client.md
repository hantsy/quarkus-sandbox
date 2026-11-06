# Jackson on REST clients

Several declarative and programmatic REST clients use Jackson extensions.
This page covers client-side codecs; server-side Jackson customization is
described in [Part 2](../part-2-rest-backends/jackson.md).

## RESTEasy Classic and JAX-RS clients

The `restclient-classic-kotlin` and `restclient-classic-jaxrs` modules use
`quarkus-resteasy-client-jackson`. They pair the classic RESTEasy client with
Jackson serialization.

## Quarkus REST client

The `restclient` and `restclient-kotlin-co` modules use
`quarkus-rest-client-jackson`. The client extension supplies Jackson support
for entity responses.

## Check the selected extension

Use the extension matching the REST stack:

- `quarkus-resteasy-client-jackson` for RESTEasy Classic clients.
- `quarkus-rest-client-jackson` for Quarkus REST clients.

The
[`restclient-classic-jaxrs` POM](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient-classic-jaxrs/pom.xml)
and
[`restclient` POM](https://github.com/hantsy/quarkus-sandbox/blob/master/restclient/pom.xml)
show these dependencies in context.

## What's next

- See [Jackson on REST APIs](../part-2-rest-backends/jackson.md) for a server
  customizer.
- See [JSON-B on REST clients](./jsonb-client.md).
