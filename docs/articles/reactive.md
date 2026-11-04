# Building Reactive APIs with Quarkus

As you know, Quarkus has built-in Jaxrs support with RESTEasy when you are creating a Quarkus application.  

Most of developers are familiar with the imperative programming methods like this.

```java
@GET
@Produces(MediaType.TEXT_PLAIN)
public String hello() {
    return "hello";
}
```

In Quarkus, you can use ReactiveStreams `Publisher` as return type.

```java
@GET
@Produces(MediaType.TEXT_PLAIN)
public Publisher<String> hello() {
    return "hello";
}
```

Besides the standard ReactiveStreams APIs, you can also use Java 8 CompletableFuture,  RxJava 2, and [SmallRye Munity](https://smallrye.io/smallrye-mutiny/).

For the database connecting to the underlay database, you can switch to the *Reactive PostgresSQL Client* or *Reactive MySQL Client*.

> Currently Hibernate core and JPA do not support ReactiveStreams APIs, but there is a new sub project under Hibernate which is trying to do this problem, see [Hibernate RX](https://github.com/hibernate/hibernate-rx).

In this post, we will refactor the sample used in [the former post](https://medium.com/@hantsy/kickstart-your-first-quarkus-application-cde54f469973), and reimplement it by using the reactive features in Quarkus. 

We will start with SmallRye Munity which may be new to developers including me, and then we will explore the Java 8 CompletableFuture and RxJava 2.

First of all, create a Quarkus project quickly using [Quarkus coding](https://code.quarkus.io) if you want to work on a new project, and add *Resteasy Munity*, *Reactive Pg Client* to dependencies.

Or running the following command to add required Quarkus extensions to the existing Quarkus project. 

```bash
mvn quarkus:add-extension -Dextension=resteasy-mutiny 
mvn quarkus:add-extension -Dextension=reactive-pg-client
```

SmallRye Munity implements the ReactiveStreams specification, and provides two conventional classes - `Uni` and `Multi`.

* Uni - handle stream of *0..1* items
* Multi - handle stream of *0..n* items (potentially unbounded)

The *Reactive Pg Client* is derived from *Vertx Reactive Pg Client*, with the help of Quarkus, you can configure database connection in the `application.properties` directly, and inject the reactive Postgres Client `PgPool` bean in your codes. 

Let's have a look at the `Post` class, there is no extra annotations on this version.

```java
public class Post {
    UUID id;
    String title;
    String content;
    LocalDateTime createdAt;
    // ...
}
```

And move to the `Repository` class, which is use for performing CRUD operations on table `posts`.

```java
@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public Uni<List<Post>> findAll() {
        return this.client
                .query("SELECT * FROM posts")
                .map(rs -> {
                    var result = new ArrayList<Post>(rs.size());
                    for (Row row : rs) {
                        result.add(this.rowToPost(row));
                    }
                    return result;
                });
    }

    public Uni<Post> findById(UUID id) {
        return this.client
                .preparedQuery("SELECT * FROM posts WHERE id=$1", Tuple.of(id))
                .map(RowSet::iterator)
                .map(it -> it.hasNext() ? rowToPost(it.next()) :null);
               // .flatMap(it -> it.hasNext() ? Uni.createFrom().item(rowToPost(it.next())) : Uni.createFrom().failure(()-> new PostNotFoundException()));
    }

    private Post rowToPost(Row row) {
        return Post.of(row.getUUID("id"), row.getString("title"), row.getString("content"), row.getLocalDateTime("created_at"));
    }

    public Uni<UUID> save(Post data) {
        return this.client
                .preparedQuery("INSERT INTO posts (title, content) VALUES ($1, $2) RETURNING (id)", Tuple.of(data.getTitle(), data.getContent()))
                .map(RowSet::iterator)
                .map(it -> it.hasNext() ? it.next().getUUID("id") : null);
    }

    public Uni<Integer> update(UUID id, Post data) {
        return this.client
                .preparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3", Tuple.of(data.getTitle(), data.getContent(), id))
                .map(RowSet::rowCount);
    }

    public Uni<Integer> deleteAll() {
        return client.query("DELETE FROM posts")
                .map(RowSet::rowCount);
    }

    public Uni<Integer> delete(UUID id) {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1", Tuple.of(id))
                .map(RowSet::rowCount);
    }

}
```

In the above codes, 

The `PgPool` is imported from package `io.vertx.mutiny.pgclient`, there are several variants for different underlay implementations.

The `preparedQuaery` method accepts a second parameter and bind them to the SQL statements.  

The `RowSet::rowCount` return the number of the affected rows when performing a update or deletion queries.

In the `findAll` and `find` methods, use `map` to transform a `RowSet` to our custom `Post` instance.

Now let's explore the changes in `PostResource`.

```java
@Path("/posts")
@RequestScoped
public class PostResource {
    private final static Logger LOGGER = Logger.getLogger(PostResource.class.getName());

    private final PostRepository posts;

    @Inject
    public PostResource(PostRepository posts) {
        this.posts = posts;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllPosts() {
        return this.posts.findAll().map(data -> ok(data).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> savePost(@Valid Post post) {
        return this.posts.save(post)
                .map(id -> created(URI.create("/posts/" + id)).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(UUID.fromString(id))
                .map(data -> {
                    if (data == null) {
                        return null;
                    }
                    return ok(data).build();
                })
                .onItem().ifNull().continueWith(status(Status.NOT_FOUND).build());
        //.onFailure(PostNotFoundException.class).recoverWithItem(status(Status.NOT_FOUND).build());
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> updatePost(@PathParam("id") final String id, @Valid Post post) {
        return this.posts.update(UUID.fromString(id), post)
                .map(updated -> updated > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") String id) {
        return this.posts.delete(UUID.fromString(id))
                .map(deleted -> deleted > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }
}
```

Yes, with the help of `resteasy-munity` extension,  you can return `Uni` or `Multi` types in the Jaxrs resources. 

Let's take  a closer look  at the `getPostById` method in the above `PostResource`, it looks a little ugly. In our `PostRepository` class, the `findById` return  a  `Uni`, when there is a `Post` found return back a `Uni<Post>`, else there is a `null` in the `Uni` stream. So in `getPostById`  we have to filter out it in the main flow and handle the null case in the `onItem().ifNull()`. For my opinion, it is a little tedious, currently there is no replacement of `switchIfEmpty` like methods in Munity. 

To make it more understandable,  use a custom exception in `PostRepository` like this.

```java
public Uni<Post> findById(UUID id) {
    return this.client
        .preparedQuery("SELECT * FROM posts WHERE id=$1", Tuple.of(id))
        .map(RowSet::iterator)
        .flatMap(it -> it.hasNext() ? Uni.createFrom().item(rowToPost(it.next())) : Uni.createFrom().failure(()-> new PostNotFoundException()));
}
```

And in the  `PostResource` class, handle this exception in the `onFailter` event.

```java
@Path("{id}")
@GET
@Produces(MediaType.APPLICATION_JSON)
public Uni<Response> getPostById(@PathParam("id") final String id) {
    return this.posts.findById(UUID.fromString(id))
        .map(data -> ok(data).build())
        .onFailure(PostNotFoundException.class).recoverWithItem(status(Status.NOT_FOUND).build());
}
```

In the *application.properties* file, configure the datasource like this.

```properties
quarkus.datasource.url = vertx-reactive:postgresql://localhost:5432/blogdb
quarkus.datasource.username = user
quarkus.datasource.password = password
```

Similar with the Jdbc datasource, but here it use the prefix `vertx-reactive:` in the connection url.

Like the former post, we will use a `DataInitializer` bean to insert some sample data at the application startup.

```java

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PgPool client;

    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        Tuple first = Tuple.of("Hello Quarkus", "My first post of Quarkus");
        Tuple second = Tuple.of("Hello Again, Quarkus", "My second post of Quarkus");

        client.query("DELETE FROM posts")
                .flatMap(result -> client.preparedBatch("INSERT INTO posts (title, content) VALUES ($1, $2)", List.of(first, second)))
                .flatMap(rs -> client.query("SELECT * FROM posts"))
                .subscribe()
                .with(
                        rows -> rows.forEach(r -> System.out.println(r)),
                        err -> System.out.println(err)
                );

    }
}
```

When the Quarkus application is started, it will raise an `StartupEvent`, the `DataInitializer` observes it and insert the data as expected.

To start a Postgres server, simply use the [docker-compose file](https://github.com/hantsy/quarkus-sample/blob/master/docker-compose.yml)  provided in  [my repos](https://github.com/hantsy/quarkus-sample/) to serve a  Postgres server in docker.

```java
docker-compose up postgres
```

It uses the predefined [initial scripts](https://github.com/hantsy/quarkus-sample/blob/master/pg-initdb.d/init.sql) to prepare the tables used in our sample at the startup stage.

Now, let's start up our application. 

Execute the following command in the root folder of the project.

```bash
mvn quarkus:dev
```

Open a terminal, and try to access the sample API endpoints using the `curl` command.

```bash
# curl http://localhost:8080/posts
[{"content":"My first post of Quarkus","createdAt":"2020-04-17T05:56:43.969994","id":"bf0bd01c-6d86-48db-9010-ef0ba263cd71","title":"Hello Quarkus"},{"content":"My second post of Quarkus","createdAt":"2020-04-17T05:56:43.969994","id":"9b13b432-cf57-4ea1-9396-59e12a310dd1","title":"Hello Again, Quarkus"}]

# curl http://localhost:8080/posts/9b13b432-cf57-4ea1-9396-59e12a310dd1
{"content":"My second post of Quarkus","createdAt":"2020-04-17T05:56:43.969994","id":"9b13b432-cf57-4ea1-9396-59e12a310dd1","title":"Hello Again, Quarkus"}

# curl http://localhost:8080/posts/9b13b432-cf57-4ea1-9396-59e12a310dd2 -v
> GET /posts/9b13b432-cf57-4ea1-9396-59e12a310dd2 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 404 Not Found
< Content-Length: 0

# curl http://localhost:8080/posts -d "{\"title\":\"my test post\", \"content\":\"my content of test post\"}" -H "Content-Type:application/json" -v

> POST /posts HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
> Content-Type:application/json
> Content-Length: 61
>

< HTTP/1.1 201 Created
< Content-Length: 0
< Location: http://localhost:8080/posts/a9916786-2e75-4a8b-8b2c-5c8f8745a4ed
<

// access the new created post.
# curl http://localhost:8080/posts/a9916786-2e75-4a8b-8b2c-5c8f8745a4ed
{"content":"my content of test post","createdAt":"2020-04-17T06:01:31.025687","id":"a9916786-2e75-4a8b-8b2c-5c8f8745a4ed","title":"my test post"}

// delete it
# curl http://localhost:8080/posts/a9916786-2e75-4a8b-8b2c-5c8f8745a4ed -X DELETE -v
> DELETE /posts/a9916786-2e75-4a8b-8b2c-5c8f8745a4ed HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 204 No Content
<

// access it again
# curl http://localhost:8080/posts/a9916786-2e75-4a8b-8b2c-5c8f8745a4ed -v
> GET /posts/a9916786-2e75-4a8b-8b2c-5c8f8745a4ed HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 404 Not Found
< Content-Length: 0
<
```

Awesome, it works as expected.

The complete codes can be found [here](https://github.com/hantsy/quarkus-sample/tree/master/mutiny).

As mentioned, in a Quarkus application, you can also use Java 8 CompletableFuture or RxJava 2 APIs if you prefer them.

Let's  have a look the version of using Java 8 CompletableFuture.

The Repository class.

```java
@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public CompletionStage<List<Post>> findAll() {
        return client.query("SELECT * FROM posts ORDER BY id ASC")
                .thenApply(rs -> StreamSupport.stream(rs.spliterator(), false)
                        .map(this::from)
                        .collect(Collectors.toList())
                );
    }

    private Post from(Row row) {
        return Post.of(row.getUUID("id"), row.getString("title"), row.getString("content"), row.getLocalDateTime("created_at"));
    }

    public CompletionStage<Post> findById(UUID id) {
        return client.preparedQuery("SELECT * FROM posts WHERE id=$1", Tuple.of(id))
                .thenApply(RowSet::iterator)
                .thenApply(iterator -> iterator.hasNext() ? from(iterator.next()) : null)
                .thenApply(Optional::ofNullable)
                .thenApply(p -> p.orElseThrow(() -> new PostNotFoundException(id)));
    }

    public CompletionStage<UUID> save(Post data) {
        return client.preparedQuery("INSERT INTO posts(title, content) VALUES ($1, $2) RETURNING (id)", Tuple.of(data.getTitle(), data.getContent()))
                .thenApply(rs -> rs.iterator().next().getUUID("id"));
    }

    public CompletionStage<Integer> update(UUID id, Post data) {
        return  client.preparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3", Tuple.of(data.getTitle(), data.getContent(), id))
                .thenApply(SqlResult::rowCount);
    }

    public CompletionStage<Integer> deleteAll() {
        return client.query("DELETE FROM posts")
                .thenApply(SqlResult::rowCount);
    }

    public CompletionStage<Integer> delete(String id) {
        return client.preparedQuery("DELETE FROM posts WHERE id=$1", Tuple.of(UUID.fromString(id)))
                .thenApply(SqlResult::rowCount);
    }

}
```

 Note :  The `PgPool` here is from package `io.vertx.axle.pgclient`.

The `PostResource` class.

```java
@Path("/posts")
@RequestScoped
public class PostResource {
    private final static Logger LOGGER = Logger.getLogger(PostResource.class.getName());

    private final PostRepository posts;

    @Inject
    public PostResource(PostRepository posts) {
        this.posts = posts;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> getAllPosts() {
        return this.posts.findAll().thenApply(posts -> ok(posts).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> savePost(@Valid Post post/*, @Context UriInfo uriInfo*/) {
        //uriInfo.getBaseUriBuilder().path("/posts/{id}").build(id.toString())
        return this.posts.save(post).thenApply(id -> created(URI.create("/posts/" + id.toString())).build());
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> updatePost(@PathParam("id") final String id, @Valid Post post) {
        return this.posts.update(UUID.fromString(id), post)
                .thenApply(updated -> updated > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .thenApply(status -> status(status).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(UUID.fromString(id))
                .thenApply(post -> ok(post).build())
                .exceptionally(throwable -> {
                    LOGGER.log(Level.WARNING, " failed to get post by id :", throwable);
                    return status(NOT_FOUND).build();
                });
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> delete(@PathParam("id") String id) {
        return this.posts.delete(id)
                .thenApply(deleted -> deleted > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .thenApply(status -> status(status).build());
    }

}
```

The complete codes of Java 8 version can be found [here](https://github.com/hantsy/quarkus-sample/tree/master/java8-cs).

Let's take a quick look at the RxJava 2 version.

The `PostRepository` class.

```java
@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final PgPool client;

    @Inject
    public PostRepository(PgPool _client) {
        this.client = _client;
    }

    public Flowable<Post> findAll() {
        return this.client
                .rxBegin()
                .flatMapPublisher(
                        tx -> tx.rxPrepare("SELECT * FROM posts")
                                .flatMapPublisher(
                                        preparedQuery -> preparedQuery.createStream(50, Tuple.tuple())
                                                .toFlowable()
                                )
                                .doAfterTerminate(tx::rxCommit)

                )
                .map(this::rowToPost);
    }

    public Maybe<Post> findById(UUID id) {
        return this.client
                .rxPreparedQuery("SELECT * FROM posts WHERE id=$1", Tuple.of(id))
                .map(RowSet::iterator)
                .flatMapMaybe(it -> it.hasNext() ? Maybe.just(rowToPost(it.next())): Maybe.empty());
    }

    private Post rowToPost(Row row) {
        return Post.of(row.getUUID("id"), row.getString("title"), row.getString("content"), row.getLocalDateTime("created_at"));
    }

    public Single<UUID> save(Post data) {
        return this.client
                .rxBegin()
                .flatMap(
                        tx -> tx.rxPreparedQuery("INSERT INTO posts (title, content) VALUES ($1, $2) RETURNING (id)", Tuple.of(data.getTitle(), data.getContent()))
                                .toFlowable().firstOrError()
                                .doAfterTerminate(tx::rxCommit)
                )
                .map(RowSet::iterator)
                .map(it -> it.hasNext() ? it.next().getUUID("id") : null);
    }

    public Single<Integer> update(UUID id, Post data) {
        return this.client
                .rxPreparedQuery("UPDATE posts SET title=$1, content=$2 WHERE id=$3", Tuple.of(data.getTitle(), data.getContent(), id))
                .map(RowSet::rowCount);
    }

    public Single<Integer> deleteAll() {
        return client.rxQuery("DELETE FROM posts")
                .map(RowSet::rowCount);
    }

    public Single<Integer> delete(UUID id) {
        return client.rxPreparedQuery("DELETE FROM posts WHERE id=$1", Tuple.of(id))
                .map(RowSet::rowCount);
    }

}
```

Note, in this class, the `PgPool` is from  package `io.vertx.reactivex.sqlclient`.

The `PostResource` class.

```java
@Path("/posts")
@RequestScoped
public class PostResource {
    private final static Logger LOGGER = Logger.getLogger(PostResource.class.getName());

    private final PostRepository posts;

    @Inject
    public PostResource(PostRepository posts) {
        this.posts = posts;
    }

    @GET
    @Path("test/maybe")
    @Produces(MediaType.APPLICATION_JSON)
    public Maybe<Integer> testMaybe() {
        return Maybe.just(1);
    }

    @GET
    @Path("test/completable")
    @Produces(MediaType.APPLICATION_JSON)
    public Completable testCompletable() {
        return Completable.fromObservable(Observable.just(1, 2, 3));
    }

    @GET
    @Path("test/flowable")
    @Produces(MediaType.APPLICATION_JSON)
    public Flowable<Integer> testFlowable() {
        return Flowable.just(1, 2, 3);
    }

    @GET
    @Path("test/single")
    @Produces(MediaType.APPLICATION_JSON)
    public Single<Integer> testSingle() {
        return Single.just(1);
    }

    @GET
    @Path("test/observable")
    @Produces(MediaType.APPLICATION_JSON)
    public Observable<Integer> testObservable() {
        return Observable.just(1, 2, 3);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Flowable<Post> getAllPosts() {
        return this.posts.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Single<Response> savePost(@Valid Post post) {
        return this.posts.save(post)
                .map(id -> created(URI.create("/posts/" + id)).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Single<Response> getPostById(@PathParam("id") final String id) {
        return this.posts.findById(UUID.fromString(id))
                .map(data-> ok(data).build())
                .switchIfEmpty(Single.just(status(Status.NOT_FOUND).build()));
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Single<Response> updatePost(@PathParam("id") final String id, @Valid Post post) {
        return this.posts.update(UUID.fromString(id), post)
                .map(updated -> updated > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }

    @DELETE
    @Path("{id}")
    public Single<Response> delete(@PathParam("id") String id) {
        return this.posts.delete(UUID.fromString(id))
                .map(deleted -> deleted > 0 ? Status.NO_CONTENT : Status.NOT_FOUND)
                .map(status -> status(status).build());
    }
}

```

The complete codes of the RxJava 2 version can be found [here](https://github.com/hantsy/quarkus-sample/tree/master/rxjava2).



