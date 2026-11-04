# Customize Jackson

The `rest` module uses Jackson with Quarkus REST and customizes its `JsonMapper`
through a CDI bean. The `multipart` module also customizes Jackson, with
additional empty-value inclusion settings.

## Customize date and time output

The
[`rest/CustomObjectMapper`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/CustomObjectMapper.java)
implements `JsonMapperBuilderCustomizer`. It disables date and time timestamp
features:

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

The `rest` POM includes `quarkus-rest-jackson`. Check that the selected
customizer and the JSON extension match the REST stack used by your module.

## Compare the multipart Jackson settings

The
[`multipart/ObjectMapperConfig`](https://github.com/hantsy/quarkus-sandbox/blob/master/multipart/src/main/java/com/example/demo/ObjectMapperConfig.java)
also disables date timestamps and changes default property inclusion to
`NON_EMPTY` for values and contents. See [Receive and send multipart
data](./multipart.md) for the endpoints that use that configuration.

## What's next

- Compare [JSON-B customization](./jsonb.md).
- See [REST backends](./index.md) for the sample modules using each provider.
