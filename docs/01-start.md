# Build a REST API with Quarkus

Build and run the posts API in the repository's `rest` module. This guide is
for developers who know Java and want a first look at Quarkus REST, Hibernate
Reactive, and Panache. It follows the existing sample rather than generating a
different project.

## Before you begin

You need:

- Java 25. The module sets `maven.compiler.release` to `25`.
- A container runtime if you want Quarkus Dev Services to start PostgreSQL.
- A terminal in the repository root.

The sample uses Quarkus `4.0.0.Beta2`. Each module owns its Maven wrapper and
configuration, so run commands from the module directory.

## Add the post model and repository

The [`Post`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/Post.java)
entity maps a post to the database. It has a generated UUID, title, content,
and creation time. The
[`CreatePostRequest`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/CreatePostRequest.java)
record validates incoming JSON: `title` cannot be blank and can contain at
most 100 characters; `content` cannot be blank.

The
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/PostRepository.java)
uses `PanacheRepositoryBase<Post, UUID>`. It provides persistence operations
and custom methods for listing and searching posts with Mutiny return types.
Open the repository source to follow how `Uni` and `Multi` carry asynchronous
results.

## Expose the resource

The
[`PostResources`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/PostResources.java)
class maps the `/posts` resource. Its POST handler accepts the validated
request, maps it to the entity, persists the entity, and returns a `201 Created`
response with a `Location` header:

```java
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Uni<Response> savePost(@Valid CreatePostRequest formData) {
    var post = Post.builder()
            .title(formData.title())
            .content(formData.content())
            .build();
    return this.posts.persist(post)
            .map(saved -> created(URI.create("/posts/" + saved.id)).build());
}
```

The resource also exposes `GET /posts` and `GET /posts/{id}`. Use the source
and tests to trace how the resource maps repository results to HTTP responses.

## Start the sample

1. Change to the `rest` module:

   ```shell
   cd rest
   ```

2. Start the application in development mode:

   ```shell
   ./mvnw quarkus:dev
   ```

   On Windows, run `mvnw.cmd quarkus:dev`.

The module uses Hibernate Reactive with Panache and the reactive PostgreSQL
client. Its datasource kind is PostgreSQL; the active configuration does not
set a connection URL. Use a container runtime for Dev Services or configure a
PostgreSQL connection for your environment. The sample configures Hibernate
schema generation as `drop-and-create`, so treat its database as disposable.

## Create a post

When the development server is running, send a JSON request to
`http://localhost:8080/posts`. In PowerShell, run:

```powershell
curl.exe -i -X POST http://localhost:8080/posts `
  -H "Content-Type: application/json" `
  --data-raw '{"title":"Quarkus cookbook","content":"My first post"}'
```

The response has status `201` and a `Location` header for the created post.
To list posts, run:

```powershell
curl.exe http://localhost:8080/posts
```

## Run the tests

Stop development mode, then run the module's tests:

```shell
./mvnw test
```

On Windows, run `mvnw.cmd test`. The module includes a
[`PostRepositoryTest`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/test/java/com/example/PostRepositoryTest.java).

## What's next

- See [Build the same API with Spring extensions](./02-spring.md) for the
  Spring-based alternative on Quarkus.
- Compare persistence choices in [REST backends and data access](./rest-backends.md).
