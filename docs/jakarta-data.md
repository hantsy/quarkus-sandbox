# Integrating Jakarta Data with Quarkus 

For relational database persistence support, Quarkus has already integrated [Hibernate ORM](https://quarkus.io/guides/hibernate-orm), [Hibernate Reactive](https://quarkus.io/guides/hibernate-reactive), and [Hibernate ORM Panache](https://quarkus.io/guides/hibernate-orm-panache).
Hibernate ORM Panache provides a generic `Repository` pattern that similar to the existing popular frameworks, such as [Spring Data JPA](https://spring.io/projects/spring-data-jpa), [Micronaut Data](https://micronaut-projects.github.io/micronaut-data/latest/guide/), etc.  Quarkus expands the Panache Repository pattern to none-relational database, such as MongoDb etc. But they do not has the common APIs as Spring Data Commons.

[Jakarta Data specification](https://jakarta.ee/specifications/data/) tries to define a collection of common APIs to access relational databases and none-relational databases. As planned, Jakarta Data 1.0 is part of [Jakarta EE 11](https://jakarta.ee/specifications/platform/11/). 

> [Jakarta Data 1.0 was just released](https://x.com/1ovthafew/status/1799120632694665660), check [specification documentation](https://jakarta.ee/specifications/data/1.0/jakarta-data-1.0).

> If you are looking an integration solution for Spring framework, check [Integrating Jakarta Data with Spring](https://medium.com/itnext/integrating-jakarta-data-with-spring-0beb5c215f5f) instead.

In this post, we will utilize the existing Hibernate ORM extension and try to integrate Jakarta Data with Quarkus.

Firstly create a simple Quarkus project via Quarkus Code. Open your browser, navigate to https://code.quarkus.io/, add the following extensions.

* Hibernate ORM
* JDBC Drivers-Postgres
* Resteasy Classic
* Resteasy Classic Jackson

Hint the *Generate your application* button, then download the generated project archive, and extract the files into your local disk, then import into your favorite IDE, eg. JetBrains Intellij IDEA. 

Open the *pom.xml* file in the project root, and add the following dependencies.

```xml
<properties>
    <hibernate.version>6.6.0.Alpha1</hibernate.version>
    // ...
</properties>

<dependencies>
    // ...
    <dependency>
        <groupId>jakarta.data</groupId>
        <artifactId>jakarta.data-api</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>${hibernate.version}</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-jpamodelgen</artifactId>
        <version>${hibernate.version}</version>
        <optional>true</optional>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```

We add the Jakarta Data API into the dependency, we will use the new Jakarta Data APIs to implement data persistence. 

Here we update Hibernate ORM to the latest `6.6.0.Alpha1` to replace the one by managed by Quarkus BOM. And also add [Lombok](https://lombokproject.org) to erase the tedious getters/betters, `equals`/`hashCode`, `toString` methods, append builder, etc. for POJO classes.

Add Lombok and Hiberante jpamodelgen annotation processors into the `configuration/annotationProcessorPaths` node in the Maven compiler plugin. 

```xml
<build>
    <plugins>
        // ...
        <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven-compiler-plugin.version}</version>
            <configuration>
                <annotationProcessorPaths>
                    <annotationProcessorPath>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </annotationProcessorPath>
                    <annotationProcessorPath>
                        <groupId>org.hibernate.orm</groupId>
                        <artifactId>hibernate-jpamodelgen</artifactId>
                        <version>${hibernate.version}</version>
                    </annotationProcessorPath>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
```

Ok, let's create some example codes.

Firstly, create two `@Entity` classes, `Post` and `Comment`, which are a one-to-many relation.

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Basic(optional = false)
    String title;

    @Basic(optional = false)
    String content;

    @Enumerated
    @Builder.Default
    Status status = Status.DRAFT;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // cascade does not work in StatelessSession
    // see:https://docs.jboss.org/hibernate/orm/6.6/repositories/html_single/Hibernate_Data_Repositories.html#programming-model
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Comment> comments;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime lastModifiedAt;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Basic(optional = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

Next, create two `Repository` classes for these `@Entity` classes respectively. The Repository interfaces extend from the Jakarta Data `CrudRepository`. 

```java
@Repository
public interface PostRepository extends CrudRepository<Post, UUID> {

}

@Repository
public interface CommentRepository extends CrudRepository<Comment, UUID> {
    
}
```

Open `application.properties` in your editor, add the following property to specify the database type we will use.

```properties
quarkus.datasource.db-kind=postgresql
```

Now compile the project, it will generate the two `Repository` implementation classes in the *target/generated-sources/annotations* folder.

Open the `PostRepository_.java`, you will see, unlike the Quarkus Panache or Spring Data JPA, here it uses the Hibernate `StatelessSession` to implement the `PostRepository` interface.

```java
@RequestScoped
@Generated("org.hibernate.processor.HibernateProcessor")
public class PostRepository_ implements PostRepository {
	protected @Nonnull StatelessSession session;
	
	@Inject
	public PostRepository_(@Nonnull StatelessSession session) {
		this.session = session;
	}

    //...
}
```

In Quarkus, all `@Repository` classes are recognized by the built-in CDI container at runtime, and they can be injected like other CDI beans, no need extra bean registration as we have done in Spring integration. 

Let's create a `DataInitializer` bean to initialize sample data at the application startup. Inject the `PostRepository` and `CommentRepository` via `@Inject`, observes a `StartupEvent` to ensure it is called at the application startup.

```java
@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    PostRepository posts;

    @Inject
    CommentRepository comments;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        Post first = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").build();
        Post second = Post.builder().title("Hello Again, Quarkus").content("My second post of Quarkus").build();

        this.posts.insertAll(List.of(first, second));
        this.posts.findAll()
                .forEach(p -> {
                    LOGGER.log(Level.INFO, "Post: {0}", new Object[]{p});
                    var comment = Comment.builder()
                            .content("Test Comment at " + LocalDateTime.now())
                            .post(p)
                            .build();
                    this.comments.insert(comment);
                });

        this.posts.findAll().forEach(p -> LOGGER.log(Level.INFO, "Post: {0}", new Object[]{p}));
        this.comments.findAll().forEach(c -> LOGGER.log(Level.INFO, "Comment: {0}", new Object[]{c}));
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
```

Open your terminal, switch to the project root folder, run the following command to start up the application in development mode.

```bash 
mvn clean quarkus:dev
```

We do not configure the datasource connection info in the *application.properties* file, when starting the application in development mode, Quarkus will startup a Postgres dev service firstly, and setup the datasource connection in the background. 

> To use Quarkus Dev Services, ensure you have installed [Docker Desktop](https://www.docker.com/products/docker-desktop/) or [Podman](https://podman.io/). More details, check [Quarkus Dev Services](https://quarkus.io/guides/dev-services).

> I encountered a weird issue when upgrading to use Quarkus 3.11 here, check [quarkus#40932](https://github.com/quarkusio/quarkus/issues/40932). Add a property `quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQLDialect` into the *application.properties* explicitly to overcome this issue temporarily.

The following codes demonstrate using the `Repository` built-in methods to insert some sample data and display the saved data.

```java
@Inject
private PostRepository posts;

// ...
Post first = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").build();
Post second = Post.builder().title("Hello Again, Quarkus").content("My second post of Quarkus").build();

this.posts.insertAll(List.of(first, second));

this.posts.findAll().forEach(p -> LOGGER.log(Level.INFO, "Post:{0}", p));
assertEquals(2, this.posts.findAll().toList().size(), "result list size is 2");
```

> Check the complete sample codes in [`PostRepositoryTest`](https://github.com/hantsy/quarkus-sandbox/blob/master/jakarta-data/src/test/java/com/example/PostRepositoryTest.java).

In the [Integrating Jakarta Data with Spring](https://medium.com/itnext/integrating-jakarta-data-with-spring-0beb5c215f5f)we encountered the transaction management issue when using Jakarta Data with Spring because Spring has not handled the transaction case of using Hibernate `StatelessSession`. 

In Quarkus, the transaction management works seamlessly with the Jakarta Data Repository interfaces.

Add a `@Transactional` annotation on the `PostRepository` interface or on the certain methods, eg. `deleteAll()`, when it is compiled, it will be added in the generated implementation class too.

```java
@Repository
public interface PostRepository extends CrudRepository<Post, UUID> {

    @Delete
    @Transactional
    void deleteAll();
}
```

In `PostRepositoryTest`, create a `@BeforeEach` hook method, we can use it to clean up the sample data for all tests.

```java
@BeforeEach
public void setup() {
    this.posts.deleteAll();
}
```

We have set `@OneToMany(cascade = CascadeType.ALL...` on the `comments` field, but this does not work when using Jakarta Data Repository because `StatelessSession` is lack of cascade support. If there are some `Comment` dirty data that exists in the `comments` table, the above invoking `deleteAll()` will fail the tests due to the foreign key constraints in the `comments` table.

> More details about Jakarta Data implementation in Hibernate, check [Hibernate Data Repositories](https://docs.jboss.org/hibernate/orm/6.6/repositories/html_single/Hibernate_Data_Repositories.html).

To enable *on delete* cascade on `comments` foreign key(`post_id`), add a Hibernate specific `@OnDelete(action = OnDeleteAction.CASCADE)` on the `comments` field. 

Then `PostRepository.deleteAll()` will work well as expected. Let's enable Hibernate SQL log to check what happened.

Add `quarkus.hibernate-orm.log.sql=true` to the *application.properties* file. And then run `PostRepositoryTest`, watch the logs in the IDE console, the following sql is executed after the `posts` and `comments` tables are created.

```sql
alter table if exists comments 
       add constraint FKh4c7lvsc298whoyd4w9ta25cr 
       foreign key (post_id) 
       references posts 
       on delete cascade
```
As you see, it adds a `on delete cascade` clause on the foreign key constraint. 

As I mentioned in the post [Integrating Jakarta Data with Spring](https://medium.com/itnext/integrating-jakarta-data-with-spring-0beb5c215f5f), utilizing the Jakarta Data built-in annotations, eg. `@Query`, `@Find`(and `@By`, `@Param`, `@OrderBy`, and `Order`, `Limit`, `PageRequest` method parameters), `@Insert`/`@Save`/`@Update`, `@Delete`, etc., Jakarta Data allows you create custom methods freely in the Repository interface or a standalone interface.

Let's create a simple interface `Blogger` which is used to manage the blog posts and comments, do not forget to annotate it with the Jakarta Data `@Repository`.

```java
@Transactional
@Repository
public interface Blogger {
    // ...
}
```

To perform basic CRUD operations on Entity classes, just need to add `@Find`, `@Insert`, `@Update`, `@Delete` annotations on the methods that the input parameter or result type matches Entity type.

```java
@Find
Optional<Post> byId(UUID id);

@Insert
Post insert(Post post);

@Insert
Comment insert(Comment comment);

@Update
Post update(Post post);

@Delete
void delete(Post post);
```

To return a pageable result, use `Page` as result type instead which accepts parameter type to indicate the result data type. And set `PageRequest` as one of the method parameters.

```java
@Find
@OrderBy("createdAt")
Page<Post> byTitle(@Pattern String title, PageRequest page);
```

The `@OrderBy` will sort the query result by the specified property in the generated query.

Alternatively, you can specify an `Order` parameter to the query result data.  

To get a smaller chunk of a large result list, you can use a `Limit` parameter to specify the data range by position.

```java
@Find
List<Post> byStatus(Status status, Order<Post> order,  Limit limit);
```

The following is an example using `@Query` and [JDQL(Jakarta Data Query Language)](https://jakarta.ee/specifications/data/1.0/jakarta-data-1.0#_jakarta_data_query_language). This method accepts a parameter named *title* and an extra `PageRequest` for pagination request, and return a paginated result. The query result will be mapped to result parameterized type `PostSummary` automatically.

```java
@Query("""
        SELECT p.id, p.title, size(c) FROM Post p LEFT JOIN p.comments c
        WHERE p.title LIKE :title
            OR p.content LIKE :title
            OR c.content LIKE :title
        GROUP BY p
        ORDER BY p.createdAt DESC
        """)
Page<PostSummary> allPosts(@Param("title") String title, PageRequest page);
```
The [`BloggerTest`](https://github.com/hantsy/quarkus-sandbox/blob/master/jakarta-data/src/test/java/com/example/BloggerTest.java) shows the usage of these custom methods.

```java
 var blog = blogger.insert(Post.builder().title("Jakarta Data").content("content of Jakarta Data").build());
blogger.insert(Comment.builder().post(blog).content("test comment").build());

blogger.insert(Post.builder().title("Quarkus and Jakarta Data").content("content of Quarkus and Jakarta Data").build());

var byTitlePattern = blogger.byTitle("%Jakarta%", PageRequest.ofPage(1, 10, true));
assertThat(byTitlePattern.totalElements()).isEqualTo(2);
byTitlePattern.content().forEach(post -> log.debug("byTitlePattern post: {}", post));

var byStatusPattern = blogger.byStatus(Status.DRAFT, Order.by(List.of(Sort.desc("createdAt"), Sort.asc("title"))), Limit.range(1, 10));
assertThat(byStatusPattern.size()).isEqualTo(2);
byStatusPattern.forEach(post -> log.debug("byStatusPattern post: {}", post));

var pagedPosts = blogger.allPosts("%Jakarta%", PageRequest.ofPage(1, 10, true));
assertThat(pagedPosts.totalElements()).isEqualTo(2);
pagedPosts.content().forEach(post -> log.debug("post: {}", post));
```        

Get [the complete sample codes](https://github.com/hantsy/quarkus-sandbox/blob/master/jakarta-data) from my Github, and experience the Jakarta Data features yourself. 









