# Customize JSON serialization

The REST samples use JSON-B or Jackson. Choose the provider based on the
extension in the module's `pom.xml`, then use that provider's customization
API. This page covers the examples in this repository.

## Customize Jackson

The [`rest`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest)
module adds `quarkus-rest-jackson`. Its
[`CustomObjectMapper`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/CustomObjectMapper.java)
implements `JsonMapperBuilderCustomizer` and disables timestamp-based
date-time serialization:

```java
@ApplicationScoped
public class CustomObjectMapper implements JsonMapperBuilderCustomizer {

    @Override
    public void customize(JsonMapper.Builder builder) {
        builder.disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        builder.disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }
}
```

The [`rest-hibernate`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-hibernate)
module also uses `quarkus-rest-jackson`, without the customizer shown above.

## Customize JSON-B

The [`rest-classic`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-classic)
module uses `quarkus-resteasy-jsonb`. Its
[`CustomJsonbConfig`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest-classic/src/main/java/com/example/config/CustomJsonbConfig.java)
implements `JsonbConfigCustomizer`. It enables formatted output, omits null
values, configures property visibility for records, and selects UTF-8 encoding.

The [`rest-pgclient`](https://github.com/hantsy/quarkus-sandbox/tree/master/rest-pgclient)
module uses `quarkus-rest-jsonb` without a JSON-B customizer. The
[`restclient-classic`](https://github.com/hantsy/quarkus-sandbox/tree/master/restclient-classic)
module uses JSON-B for its RESTEasy Classic client and server.

## Match the extension to the REST stack

The repository uses these JSON extensions:

- `quarkus-rest-jsonb` with Quarkus REST.
- `quarkus-rest-jackson` with Quarkus REST.
- `quarkus-resteasy-jsonb` with RESTEasy Classic.
- `quarkus-resteasy-jackson` with RESTEasy Classic.

Use the extension and customizer from the same REST stack as your application.
The [REST backend samples](./rest-backends.md) show which module uses each
combination.

## What's next

- See [REST backends and data access](./rest-backends.md) for persistence
  samples.
- See [REST clients](./rest-clients.md) for outbound JSON clients.
