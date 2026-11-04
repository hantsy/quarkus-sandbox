# Build the API with Spring extensions

Use the `spring` module to build the posts API with Spring Web, Spring
Dependency Injection, and Spring Data JPA running on Quarkus. This guide is
for developers who know the Spring programming model. The sample uses Quarkus
extensions; it is not a Spring Boot application.

## Before you begin

You need Java 25, a container runtime for local PostgreSQL Dev Services, and a
terminal in the repository root. The module sets the compiler release to
Java 25 and uses Quarkus `4.0.0.Beta2`.

## Start the sample

1. Change to the `spring` module:

   ```shell
   cd spring
   ```

2. Start the application in development mode:

   ```shell
   ./mvnw quarkus:dev
   ```

   On Windows, run `mvnw.cmd quarkus:dev`.

The sample's `application.properties` selects PostgreSQL and configures
Hibernate schema generation as `drop-and-create`. The connection URL is not
set in the active configuration. Use Dev Services with a container runtime or
configure a PostgreSQL connection for your environment. Use a disposable
database for this sample.

## Inspect the Spring API

The
[`PostController`](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/src/main/java/com/example/PostController.java)
uses Spring MVC annotations to expose the posts routes. `GET /posts` returns
a page, `GET /posts/{id}` reads a post, and the `POST`, `PUT`, and `DELETE`
handlers create, update, and remove posts.

The
[`PostRepository`](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/src/main/java/com/example/PostRepository.java)
extends Spring Data's `JpaRepository`. Its custom interface and implementation
provide keyword search. The
[`spring` POM](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/pom.xml)
declares the Quarkus Spring Web, Spring DI, Spring Data JPA, and PostgreSQL
extensions.

The list endpoint returns a Spring Data `Page<Post>` and accepts `page` and
`size` query parameters. This response shape differs from the Quarkus REST
sample's list response.

The controller's create handler accepts a validated request object:

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

The request type validates that `title` and `content` are not empty. The
controller returns `201 Created` and a `Location` header for the new post.

## Verify the API

With the application running, send a JSON request to
`http://localhost:8080/posts`. In PowerShell, run:

```powershell
curl.exe -i -X POST http://localhost:8080/posts `
  -H "Content-Type: application/json" `
  --data-raw '{"title":"Quarkus with Spring","content":"A sample post"}'
```

The response has status `201` and a `Location` header. The
[`PostControllerTest`](https://github.com/hantsy/quarkus-sandbox/blob/master/spring/src/test/java/com/example/PostControllerTest.java)
and `IntegrationTest` in the module exercise the API.

To run the tests, stop development mode and run:

```shell
./mvnw test
```

On Windows, run `mvnw.cmd test`.

## What's next

- Compare this API with [the Quarkus REST implementation](./01-start.md).
- See [REST backends and data access](./rest-backends.md) for other
  persistence models.
