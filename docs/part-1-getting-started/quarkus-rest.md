# Build a REST API with Quarkus REST

Build and run the posts API in the repository's `rest` module. This recipe is
for developers who know Java and want to follow a Quarkus REST resource through
Hibernate Reactive with Panache. It covers this repository's sample rather
than generating a different project.

## Before you begin

You need Java 25 and a container runtime if you want Quarkus Dev Services to
start PostgreSQL. The module sets `maven.compiler.release` to `25` and uses
Quarkus `4.0.0.CR1`.

The active datasource configuration sets the database kind to PostgreSQL but
does not set a connection URL. Use Dev Services with a container runtime or
configure a PostgreSQL connection. Hibernate schema generation is
`drop-and-create`; use a disposable database.

## Follow the API structure

The [`Post`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/Post.java)
entity maps a post to the database. The
[`CreatePostRequest`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/CreatePostRequest.java)
record validates incoming JSON: `title` cannot be blank and is limited to 100
characters; `content` cannot be blank.

The
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/PostRepository.java)
uses `PanacheRepositoryBase<Post, UUID>` and returns Mutiny `Uni` and `Multi`
values. The
[`PostResources`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/main/java/com/example/PostResources.java)
class maps the `/posts` resource to repository operations.

The create handler validates the request, persists a post, and returns a
`201 Created` response with a `Location` header:

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

The resource also exposes `GET /posts`, `GET /posts/{id}`, `PUT /posts/{id}`,
and `DELETE /posts/{id}`.

## Start the application

1. Change to the `rest` module:

   ```shell
   cd rest
   ```

2. Start development mode:

   ```shell
   ./mvnw quarkus:dev
   ```

   On Windows, run `mvnw.cmd quarkus:dev`.

## Create and read a post

When the server is running, send a JSON request to
`http://localhost:8080/posts`. In PowerShell, run:

```powershell
curl.exe -i -X POST http://localhost:8080/posts `
  -H "Content-Type: application/json" `
  --data-raw '{"title":"Quarkus cookbook","content":"My first post"}'
```

The response has status `201` and a `Location` header. To list posts, run:

```powershell
curl.exe http://localhost:8080/posts
```

## Verify the sample

Stop development mode, then run the module's tests:

```shell
./mvnw test
```

On Windows, run `mvnw.cmd test`. The module includes
[`PostRepositoryTest`](https://github.com/hantsy/quarkus-sandbox/blob/master/rest/src/test/java/com/example/PostRepositoryTest.java).

## What's next

- Compare the Spring programming model in [Build a Spring API on
  Quarkus](./spring.md).
- See [Hibernate Reactive with Panache](../part-2-rest-backends/hibernate-reactive-panache.md)
  for more backend details.
