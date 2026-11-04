# Customize JSON-B

The `rest-classic` sample uses JSON-B with RESTEasy Classic and customizes
serialization through a CDI bean. Use this recipe to locate that
customization; check the module's dependency versions before adapting it.

## Configure JSON-B

The module includes `quarkus-resteasy-jsonb`. Its
[`CustomJsonbConfig`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-classic/src/main/java/com/example/config/CustomJsonbConfig.java)
implements `JsonbConfigCustomizer`. The customizer enables formatted output,
omits null values, sets UTF-8 encoding, and installs a property visibility
strategy that exposes fields rather than methods. This supports record
serialization in the sample.

The `rest-pgclient` module also uses JSON-B through `quarkus-rest-jsonb`, but
does not add the customizer shown here.

## Run the sample

From the `rest-classic` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Use the module's database
configuration before calling the resources.

## What's next

- See [Customize Jackson](./jackson.md) for the corresponding Jackson sample.
- Compare JSON extensions in [REST backend index](./index.md#json-and-request-bodies).
