# Consuming GraphQL APIs with Quarkus

In [the last post](https://hantsy.medium.com/building-graphql-apis-with-quarkus-dbbf23f897df), we have built a simple GraphQL API example, now let's discuss how to use GraphQL Client to interact with the backend GraphQL APIs.



## Generating Project Skeleton

Create a  Quarkus project using [Quarkus Code Generator](https://code.quarkus.io), import the source codes into your IDE.

Open *pom.xml* file, add the following dependencies.

 ```xml
 <dependency>
     <groupId>io.quarkus</groupId>
     <artifactId>quarkus-smallrye-graphql-client</artifactId>
 </dependency>
 <dependency>
     <groupId>io.quarkus</groupId>
     <artifactId>quarkus-rest-client</artifactId>
 </dependency>
 <dependency>
     <groupId>io.quarkus</groupId>
     <artifactId>quarkus-rest-client-jsonb</artifactId>
 </dependency>
 <dependency>
     <groupId>org.projectlombok</groupId>
     <artifactId>lombok</artifactId>
     <version>1.18.20</version>
 </dependency>
 ```

Lombok is used to erase the setters, getters, hashCode, equals, toString etc. for your POJO classes. I have used it for many years in my projects.

## Declaring  GraphQL Client API

Similar to the MicroProfile RestClient spec, SmallRye GraphQL provides the same approach to declare a GraphQL Client from an interface.

> Note, this Client API is not a part of MicroProfile GraphQL specification.

```java
@GraphQLClientApi
public interface PostGraphQLClient {
    @Query()
    public List<Post> getAllPosts() ;

    @Query
    @Description("Get a specific post by providing an id")
    public Post getPostById(@Name("postId") String id);

    @Mutation
    @Description("Create a new post")
    public Post createPost(@Valid CreatePost createPostInput);
}
```

The POJO classes used in the above codes, such as `Post`,  `Coment` and  `CreatePost` are copied from [the backend GraphQL API project](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql) we have created in the last post.

To locate which remote GraphQL API will be connected, similar to the MP RestClient API, configure the base URI of the remote GraphQL API.

```properties
com.example.demo.PostGraphQLClient/mp-graphql/url=http://localhost:8080/graphql
```

Now let's try to call the `getAllPosts` of `PostGraphQLClient` to print out all posts.  

Create a class implements `QuarkusApplication` and annotated it with `@QuarkusMain`, it will work like the main class in a general Java application.

```java
@QuarkusMain
public class Main implements QuarkusApplication {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Inject
    PostGraphQLClient clientApi;

    @Override
    public int run(String... args) throws Exception {
        this.clientApi.getAllPosts().forEach(
            p -> LOGGER.log(Level.INFO, "post: {0}", p)
        );
        return 0;
    }
}
```

Open the terminal and switch to the project root folder. Run `mvn quarkus:dev` to start the application in debug mode.
When it is started, you will see the following info in the console log.

```bash 
2021-06-03 20:21:00,505 INFO  [io.sma.gra.cli.typ.jax.JaxRsTypesafeGraphQLClientProxy] (Quarkus Main Thread) request graphql: query allPosts { allPosts {id title content countOfComment
s comments {id content}} }
2021-06-03 20:21:00,517 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post: Post(id=9ef6d49f-50bf-4973-a122-3ac56a1b8d41, title=title #1, content=test content of #1, countOfComments=2
, comments=[Comment(id=e261e9cb-f06c-4989-bbb5-00319087496d, content=comment #1), Comment(id=7664a80f-0963-46c1-8955-c096d7609c1b, content=comment #2)])
2021-06-03 20:21:00,517 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post: Post(id=0c5501eb-cb7e-42e2-9078-040af89e6310, title=title #2, content=test content of #2, countOfComments=2
, comments=[Comment(id=bf4e2553-cb59-4e87-bc8f-36360732a919, content=comment #1), Comment(id=165d6743-fd74-4ad5-8997-3853fb076403, content=comment #2)])
2021-06-03 20:21:00,517 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post: Post(id=c61c6b62-584b-455f-a2a8-1a4253f832b7, title=title #3, content=test content of #3, countOfComments=0
, comments=[])
2021-06-03 20:21:00,517 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post: Post(id=37e28b7d-11fc-4587-920f-9415da1d93a3, title=title #4, content=test content of #4, countOfComments=2
, comments=[Comment(id=5963588f-fbe0-4c82-88f6-1011bb7538fe, content=comment #1), Comment(id=feaf4d21-e78b-4701-91bb-ea665a9a034c, content=comment #2)])
```

Comparing to REST APIs, the most attractive feature of GraphQL is it only returns the required fields that are declared by client.  In the above the codes it returns all fields of a `Post`. 

Assume you just want to retrieve the *title* field in the result of executing the `allPosts` query, create a new POJO class just includes a *title* property.

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostSummary {
    String title;
}
```

Add a new method into the above `PostGraphQLClient` class.

```java
@Query("allPosts")
public List<PostSummary> getAllPostSummaries() ;
```

Let's call it and print out the result.

```java
this.clientApi.getAllPostSummaries().forEach(
    p -> LOGGER.log(Level.INFO, "post summary: {0}", p)
);
```

You can see the following info from the application log.

```bash
2021-06-03 20:21:00,533 INFO  [io.sma.gra.cli.typ.jax.JaxRsTypesafeGraphQLClientProxy] (Quarkus Main Thread) request graphql: query allPosts { allPosts {title} }
2021-06-03 20:21:00,533 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post summary: PostSummary(title=title #1)
2021-06-03 20:21:00,533 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post summary: PostSummary(title=title #2)
2021-06-03 20:21:00,533 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post summary: PostSummary(title=title #3)
2021-06-03 20:21:00,533 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post summary: PostSummary(title=title #4)
```
Yeah, it only request a *title* field in the GraphQL request, and return the exact fields in the fetched data.


## Handling Client Exceptions

Currently when calling the Client APIs, Quarkus does not provide a registry for handling exceptions like MP RestClient API, but there are some possible means to archive the purpose.

The hard way is using try/catch to handle the `GraphQLClientException`, eg. 

```java
String id = UUID.randomUUID().toString();
// catch a GraphQLClientException.
try {
    var p = this.clientApi.getPostById(id);
    LOGGER.log(Level.INFO, "post: {0}", p);
} catch (GraphQLClientException e) {
    if (e.getErrors().stream().anyMatch(error -> error.getErrorCode().equals("POST_NOT_FOUND"))) {
        throw new PostNotFoundException(id);
    }
}
```
In the above codes, if the postId is not existed, it will throw a `GraphQLClientException`.

Besides this, wrapping your response with `ErrorOr` class is an alternative approach.

```java
@Query
@Description("Get a specific post by providing an id")
public ErrorOr<Post> getPostById(@Name("postId") String id);
```

`ErrorOr` is very similar to the Java `Optional`, you can navigate one of two parts, **data** and **errors**. When the errors exists, the `getErrors` method will 
collect all errors from the backend GraphQL API in a slient way(comparing to the try/catch method).
```java
String id = UUID.randomUUID().toString();

// return a `ErrorOr` instead.
var post = this.clientApi.getPostById(id);
if (post.isPresent()) {
    LOGGER.log(Level.INFO, "found: {0}", post.get());
}
if (post.isError()) {
    post.getErrors().forEach(
        error -> LOGGER.log(Level.INFO, "error: code={0}, message={1}", new Object[]{error.getErrorCode(), error.getMessage()})
    );
}
```

When running the application, you can see the following log.

```bash
2021-06-03 20:21:00,423 INFO  [io.sma.gra.cli.typ.jax.JaxRsTypesafeGraphQLClientProxy] (Quarkus Main Thread) request graphql: query postById($arg0: String) { postById(postId: $arg0) {i
d title content countOfComments comments {id content}} }
2021-06-03 20:21:00,505 INFO  [com.exa.dem.Main] (Quarkus Main Thread) error: code=POST_NOT_FOUND, message=Post: 8c46ca01-530e-47c1-a257-e86333bcb69b was not found.
```

## Dynamic  Client

We have discussed the client using `@GraphQLClientApi`, Quarkus also provide a  **dynamic client**.  It is more like a Java translation of the GraphQL request form.

For example,  perform the following query in the [GraphQL UI](http://localhost:8080/q/graphql-ui/) to retrieve all posts.

```graphql
query {
  allPosts {
    id
    title
    content
    comments {
      id
      content
    }
  }
}
```

Create a new bean to do the same work. 

```java
@ApplicationScoped
public class PostDynamicClient {

    @Inject
    @NamedClient("post-dynamic-client")
    DynamicGraphQLClient dynamicClient;

    public List<Post> getAllPosts() throws ExecutionException, InterruptedException {
        Document query = document(
                operation(
                        field("allPosts",
                                field("id"),
                                field("title"),
                                field("content"),
                                field("comments",
                                        field("id"),
                                        field("content")
                                )
                        )
                )
        );
        Response response = dynamicClient.executeSync(query);
        return response.getList(Post.class, "allPosts");
    }
}
```

Through the `document`, `operation` and `field` static methods,  we build the same query structure in Java *dialect*. 

The value of `NamedClient` to identify different clients,  which is also used as the configuration key in the *application.properties*.

```properties
post-dynamic-client/mp-graphql/url=http://localhost:8080/graphql
```

Now try to call the  `getAllPosts` of this client and print out all posts.

```java
@Inject
PostDynamicClient dynamicClient;

this.dynamicClient.getAllPosts().forEach(
    p -> LOGGER.log(Level.INFO, "post from dynamic client: {0}", p)
);
```

Run the application and you can see the following info.

```java
1, countOfComments=0, comments=[Comment(id=e261e9cb-f06c-4989-bbb5-00319087496d, content=comment #1), Comment(id=7664a80f-0963-46c1-8955-c096d7609c1b, content=comment #2)])
2021-06-03 20:21:01,507 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post from dynamic client: Post(id=0c5501eb-cb7e-42e2-9078-040af89e6310, title=title #2, content=test content of #
2, countOfComments=0, comments=[Comment(id=bf4e2553-cb59-4e87-bc8f-36360732a919, content=comment #1), Comment(id=165d6743-fd74-4ad5-8997-3853fb076403, content=comment #2)])
2021-06-03 20:21:01,507 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post from dynamic client: Post(id=c61c6b62-584b-455f-a2a8-1a4253f832b7, title=title #3, content=test content of #
3, countOfComments=0, comments=[])
2021-06-03 20:21:01,507 INFO  [com.exa.dem.Main] (Quarkus Main Thread) post from dynamic client: Post(id=37e28b7d-11fc-4587-920f-9415da1d93a3, title=title #4, content=test content of #
4, countOfComments=0, comments=[Comment(id=5963588f-fbe0-4c82-88f6-1011bb7538fe, content=comment #1), Comment(id=feaf4d21-e78b-4701-91bb-ea665a9a034c, content=comment #2)])
```

## HttpClient

In Quarkus, the GraphQL client is shaking hands with the backend GraphQL API over HTTP protocol. Ideally if you are familiar with GraphQL interexchange format(json), you can use any HttpClient to send a *POST* request to perform the GraphQL query, such as, cURL, Resteasy Client/JAXRS Client or the simple Java 11 HttpClient. 

```java
@ApplicationScoped
public class JvmClient {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private final HttpClient httpClient = HttpClient.newBuilder()
            .executor(executorService)
            .version(HttpClient.Version.HTTP_2)
            .build();


    @Inject
    Jsonb jsonb;

    CompletionStage<List<Post>> getAllPosts() {

        // Have to erase the new line chars in the GraphQL schema to avoid the parsing exception.
        // see: https://github.com/quarkusio/quarkus/issues/17667
        var queryString = """
                {"query": "query {
                            allPosts {
                              id
                              title
                              content
                              comments {
                                id
                                content
                              }
                            }
                          }"
                }
                """.replaceAll("\\n", " ");
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(queryString))
                .uri(URI.create("http://localhost:8080/graphql"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return this.httpClient
                .sendAsync(request, handler)
                .thenApply(HttpResponse::body)
                .thenApply(this::extractPosts);

    }

    /**
     * @param s the GraphQL response, eg.
     * @formatter:off
     *          {
     *              data:{
     *                  allPosts: [
     *                      {
     *                      id:"xxxx",
     *                      title:"test title",
     *                      content:"content"
     *                      }
     *                  ]
     *              }
     *          }
     * @formatter:on
     * @return The parsed the list of post data.
     */
    List<Post> extractPosts(String s) {
        var reader = new StringReader(s);
        var json = Json.createReader(reader).read();
        var pointer = Json.createPointer("/data/allPosts");
        var jsonArray = (JsonArray) pointer.getValue(json);
        //@formatter:off
        return jsonb.fromJson(jsonArray.toString(), new TypeLiteral<List<Post>>() {}.getRawType());
        //@formatter:on
    }
}
```

>The *text block* is great to compose a multiline string, but there is [an issue](https://github.com/quarkusio/quarkus/issues/17667) which caused the schema parsing failed. I added a `replaceAll` method to erase the newline breaks.

Try to call the `getAllPosts` in this client.

```java
@Inject
JvmClient jvmClient;

this.jvmClient.getAllPosts()
    .thenAccept(
    	p -> LOGGER.log(Level.INFO, "post from jvm client: {0}", p)
	)
    .whenComplete((d, e) -> LOGGER.info("The request is done in the jvm client."))
    .toCompletableFuture()
    .join();
```

Run the application you will see the following in the application log.

```bash
2021-06-03 20:21:01,662 INFO  [com.exa.dem.Main] (ForkJoinPool.commonPool-worker-7) post from jvm client: [{comments=[{id=e261e9cb-f06c-4989-bbb5-00319087496d, content=comment #1}, {id
=7664a80f-0963-46c1-8955-c096d7609c1b, content=comment #2}], id=9ef6d49f-50bf-4973-a122-3ac56a1b8d41, title=title #1, content=test content of #1}, {comments=[{id=bf4e2553-cb59-4e87-bc8
f-36360732a919, content=comment #1}, {id=165d6743-fd74-4ad5-8997-3853fb076403, content=comment #2}], id=0c5501eb-cb7e-42e2-9078-040af89e6310, title=title #2, content=test content of #2
}, {comments=[], id=c61c6b62-584b-455f-a2a8-1a4253f832b7, title=title #3, content=test content of #3}, {comments=[{id=5963588f-fbe0-4c82-88f6-1011bb7538fe, content=comment #1}, {id=fea
f4d21-e78b-4701-91bb-ea665a9a034c, content=comment #2}], id=37e28b7d-11fc-4587-920f-9415da1d93a3, title=title #4, content=test content of #4}]
2021-06-03 20:21:01,662 INFO  [com.exa.dem.Main] (ForkJoinPool.commonPool-worker-7) The request is done in the jvm client.
```

[Get the source codes from my Github](https://github.com/hantsy/quarkus-sandbox/blob/master/graphql-client).



