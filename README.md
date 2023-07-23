# Quarkus Sandbox

A  personal sandbox project  to experience the new features of the [Quarkus framework](https://www.quarkus.io).

## Docs

* [Kickstart your first Quarkus application](./docs/01-start.md)
* [Building a Spring web application with Quarkus](./docs/02-spring.md)
* [Interacting with REST APIs ](./docs/restclient.md)
* [Building reactive APIs with Quarkus](./docs/reactive.md)
* [Building GraphQL APIs with Quarkus](./docs/graphql.md)
* [Consuming GraphQL APIs with Quarkus](./docs/graphql-client.md)

## Sample Codes

| Name | Description |
|:-------------------|--------------------------------------------------|
|[resteasy-classic](https://github.com/hantsy/quarkus-sample/tree/master/resteasy-classic) |Simple CURD RESTful APIs using tranditional Resteasy, Hibernate/JPA|
|[resteasy-reactive-pgclient](https://github.com/hantsy/quarkus-sample/tree/master/resteasy-reactive-pgclient)  |Resteasy Reactive and Reactive Postgre client|
|[resteasy-reactive](https://github.com/hantsy/quarkus-sample/tree/master/resteasy-reactive) |Resteasy Reactive, Hibernate Reactive Repository, Smallrye Mutiny example.|
|[resteasy-kotlin-co](https://github.com/hantsy/quarkus-sample/tree/master/resteasy-kotlin-co) |Resteasy Reactive/Kotlin Coroutines, Hibernate Reactive Repository, Smallrye Mutiny example.|
|[resteasy-reactive-hibernate](https://github.com/hantsy/quarkus-sample/tree/master/resteasy-reactive-hibernate) |Resteasy Reactive, Hibernate Reactive Session API, Smallrye Mutiny example.|
|[vertx-routes](https://github.com/hantsy/quarkus-sample/tree/master/vertx-routes)  |Simple CRUD RESTful APIs using Vertx `Router` and Reactive PgClient with Mutiny APIs.|
|[spring](https://github.com/hantsy/quarkus-sample/tree/master/spring) |CRUD RESTful APIs using Spring DI, Spring Data JPA, Spring WebMvc|
|[multipart](https://github.com/hantsy/quarkus-sample/tree/master/multipart)  |Multipart examples|
|[amqp](https://github.com/hantsy/quarkus-sample/tree/master/amqp)  |Reactive Messaging with AMQP connector and JAXRS SSE examples|
|[GraphQL](https://github.com/hantsy/quarkus-sample/tree/master/graphql)  |GraphQL example using MP GraphQL/Smallrye GraphQL|
|[GraphQL Client](https://github.com/hantsy/quarkus-sample/tree/master/graphql-client)  |GraphQL Client example using MP GraphQL/Smallrye GraphQL|
|[mongodb-kotlin](https://github.com/hantsy/quarkus-sample/tree/master/mongodb-kotlin) |MongoDb/Resteasy/Kotlin/Mockk example|
|[mongodb-kotlin-co](https://github.com/hantsy/quarkus-sample/tree/master/mongodb-kotlin-co) |MongoDb Reactive/Resteasy Reactive/Kotlin Coroutines example|
|[restclient-classic](https://github.com/hantsy/quarkus-sample/tree/master/restclient-classic)|Rest Client Classic Example|
|[restclient-mutiny](https://github.com/hantsy/quarkus-sample/tree/master/restclient-mutiny)|Rest Client Classic with Mutiny Example|
|[restclient-reactive](https://github.com/hantsy/quarkus-sample/tree/master/restclient-reactive)|Rest Client Reactive Example|
|[restclient-kotlin](https://github.com/hantsy/quarkus-sample/tree/master/restclient-kotlin) |Rest Client using MP RestClient spec, but written in Kotlin|
|[restclient-kotlin-co](https://github.com/hantsy/quarkus-sample/tree/master/restclient-kotlin-co) |Rest Client Reactive/Kotlin Coroutines|
|[restclient-jaxrs](https://github.com/hantsy/quarkus-sample/tree/master/restclient-jaxrs)| Rest Client using classic Jaxrs Client API|
|[restclient-java11](https://github.com/hantsy/quarkus-sample/tree/master/restclient-java11) |Rest Client using Java 11 HttpClient API|
|[restclient-vertx](https://github.com/hantsy/quarkus-sample/tree/master/restclient-vertx) |Rest Client using Vertx Mutiny WebClient API|

The following examples used Java 8 or RxJava 2 based Reactive Postgres Client which is depreacted and not available in the latest Qukarus. 

| Name | Description |
|:-------------------|--------------------------------------------------|
|[java8](https://github.com/hantsy/quarkus-sample/tree/master/legacy/java8)  |Simple CRUD RESTful APIs but using Java 8 `CompletionStage` and Reactive Postgre Client|
|[rxjava2](https://github.com/hantsy/quarkus-sample/tree/master/legacy/rxjava2) |CRUD RESTful APIs using RxJava 2 and Reactive Postgre client|
|[vertx-routes](https://github.com/hantsy/quarkus-sample/tree/master/legacy/vertx-routes)  |Simple CRUD RESTful APIs using Vertx `Router` .|
|[java8-hibernate-reactive](https://github.com/hantsy/quarkus-sample/tree/master/legacy/java8-hibernate-reactive) |Java 8 CompletablFuture, Hibernate Reactive Example.|

## References

* [Quarkus Workshop](https://quarkus.io/quarkus-workshops/super-heros/)
* [Quarkus beginners guide](https://jaxlondon.com/quarkus-beginners-guide-cheat-sheet) by Jaxcenter
* [DZone Refcard : Quarkus](https://dzone.com/refcardz/quarkus-1?chapter=1)
* [Configuring a Quarkus application](https://dzone.com/articles/configuring-a-quarkus-application?fromrel=true)
* [Thoughts on Quarkus](https://dzone.com/articles/thoughts-on-quarkus)
