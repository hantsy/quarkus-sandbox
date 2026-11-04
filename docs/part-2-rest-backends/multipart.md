# Receive and send multipart data

The `multipart` module demonstrates multipart form input and multipart output
with Quarkus REST. The two workflows below distinguish receiving a multipart
request from sending one and returning a multipart response.

## Before you begin

The module uses Java 25 and Quarkus `4.0.0.Beta2`. Its POM includes
`quarkus-rest`, `quarkus-rest-jackson`, and the REST client extensions. Start
the application from the `multipart` directory:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## Receive multipart form data

The root `POST /` resource consumes `multipart/form-data` into
[`MultipartBody`](https://github.com/hantsy/quarkus-sandbox/blob/master/multipart/src/main/java/com/example/demo/MultipartBody.java).
The form maps the `file` part to a `File`, declares its media type as
`text/csv`, and also accepts `test`, `checked`, and `choice` form fields.
[`MultipartResource`](https://github.com/hantsy/quarkus-sandbox/blob/master/multipart/src/main/java/com/example/demo/MultipartResource.java)
reads these values from the body object.

The alternative `POST /upload2` method accepts
`MultipartFormDataInput`; it demonstrates the low-level input type, but the
sample method currently returns `ok` without reading individual parts.

The `MultipartResourceTest` sends a request with those fields and the module's
`test.csv` file. From the `multipart` directory, run the focused test:

```shell
./mvnw test
```

On Windows, run `mvnw.cmd test`.

## Send multipart form data and return multipart output

The test demonstrates sending a multipart request with REST Assured. Its file
part is named `file` and uses the `text/csv` media type:

```java
given()
    .formParam("test", "a String field")
    .formParam("checked", "true")
    .formParam("choice", "YES")
    .multiPart("file", new File("test.csv"), "text/csv")
    .when().post("")
    .then()
    .statusCode(200);
```

You can send the same form from the `multipart` module directory with curl:

```powershell
curl.exe -i -X POST http://localhost:8080/ `
  -F "file=@test.csv;type=text/csv" `
  -F "test=a String field" `
  -F "checked=true" `
  -F "choice=YES"
```

The sample resource responds with `ok`. The `test.csv` file is in the
`multipart` project directory.

The resource also demonstrates a multipart response. `GET /output` returns a
`MultipartFormDataOutput` with a JSON `user` part and an XML `user` part. Send
a GET request to `http://localhost:8080/output` to inspect its response:

```powershell
curl.exe -i http://localhost:8080/output
```

The module's Jackson customizer omits empty values and serializes date-time
values without timestamps. See [Customize Jackson](./jackson.md).

## What's next

- See [the backend index](./index.md) for persistence and serialization
  recipes.
- See [REST clients](../part-3-rest-clients/index.md) for outbound HTTP
  examples.
