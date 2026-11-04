# Build a Spring API on Quarkus

Run the posts API with Spring Web, Spring Dependency Injection, and Spring
Data JPA extensions on Quarkus. This recipe is for developers familiar with
Spring APIs. The sample uses Quarkus extensions; it is not a Spring Boot
application.

## Before you begin

You need Java 25 and a container runtime for local PostgreSQL Dev Services.
The module sets the compiler release to Java 25 and uses Quarkus `4.0.0.Beta2`.

The active `application.properties` configuration selects PostgreSQL and
`drop-and-create` schema generation, but does not set a connection URL. Use
Dev Services or configure a PostgreSQL connection. Keep the sample database
disposable.

## Inspect the Spring API

The
[`PostController`](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/src/main/java/com/example/PostController.java)
uses Spring MVC annotations for `/posts`. `GET /posts` accepts `page` and
`size` and returns a Spring Data `Page<Post>`. `GET /posts/{id}` reads a post;
the create, update, and delete handlers write posts.

The
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/src/main/java/com/example/PostRepository.java)
extends Spring Data `JpaRepository`. Its custom interface and implementation
provide keyword search: `GET /posts/search?q=...` filters post title and
content using a JPA Criteria query and supports offset/limit pagination. The
module's POM includes the Quarkus Spring Web, Spring DI, Spring Data JPA, and
PostgreSQL extensions.

[`PostExceptionHandler`](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/src/main/java/com/example/PostExceptionHandler.java)
shows the sample's exception handling approaches. It maps a missing post to a
structured `404` response and demonstrates `@ExceptionHandler` methods with
`ResponseEntity`, `@ResponseStatus`, and the servlet response API.

The create handler accepts a validated request object and returns a
`201 Created` response with a `Location` header:

```java
@PostMapping()
public ResponseEntity<Void> createPost(
        @RequestBody @Valid CreatePostCommand post) {
    Post data = Post.of(post.getTitle(), post.getContent());
    Post saved = this.postRepository.save(data);

    return created(
            ResteasyUriBuilder.fromTemplate("/posts/{id}")
                    .build(saved.getId())
                    .normalize()
    ).build();
}
```

## Start and verify the application

1. Change to the `spring` module:

   ```shell
   cd spring
   ```

2. Start development mode:

   ```shell
   ./mvnw quarkus:dev
   ```

   On Windows, run `mvnw.cmd quarkus:dev`.

3. Send a request to create a post:

   ```powershell
   curl.exe -i -X POST http://localhost:8080/posts `
     -H "Content-Type: application/json" `
     --data-raw '{"title":"Quarkus with Spring","content":"A sample post"}'
   ```

   The response has status `201` and a `Location` header.

Stop development mode and run `./mvnw test` (or `mvnw.cmd test` on Windows)
to run the module's tests. The
[`PostControllerTest`](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/src/test/java/com/example/PostControllerTest.java)
and `IntegrationTest` cover the API.

## What's next

- Compare data access options in [REST backends and data access](../part-2-rest-backends/index.md).
- Return to [Build a REST API with Quarkus REST](./quarkus-rest.md).
