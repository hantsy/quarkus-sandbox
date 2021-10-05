# Quarkus Sandbox

A  personal sandbox project  to experience the new features of the [Quarkus framework](https://www.quarkus.io).

![build](https://github.com/hantsy/quarkus-sandbox/workflows/build/badge.svg)

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
|[post-service](https://github.com/hantsy/quarkus-sample/tree/master/post-service) |Simple CURD RESTful APIs using Resteasy, Hibernate|
|[spring](https://github.com/hantsy/quarkus-sample/tree/master/spring) |CRUD RESTful APIs using Spring DI, Spring Data JPA, Spring WebMvc|
|[mutiny](https://github.com/hantsy/quarkus-sample/tree/master/mutiny)  |CRUD RESTful APIs using Smallrye Mutiny and Reactive Postgre client|
|[restclient](https://github.com/hantsy/quarkus-sample/tree/master/restclient)|Rest Client using MP RestClient spec|
|[restclient-kotlin](https://github.com/hantsy/quarkus-sample/tree/master/restclient-kotlin) |Rest Client using MP RestClient spec, but written in Kotlin.|
|[restclient-jaxrs](https://github.com/hantsy/quarkus-sample/tree/master/restclient-jaxrs)| Rest Client using Jaxrs Client API.|
|[restclient-java11](https://github.com/hantsy/quarkus-sample/tree/master/restclient-java11) |Rest Client using Java 11 HttpClient API.|
|[restclient-mutiny](https://github.com/hantsy/quarkus-sample/tree/master/restclient-mutiny) |Rest Client using Vertx Mutiny WebClient API.|
|[resteasy-reactive](https://github.com/hantsy/quarkus-sample/tree/master/resteasy-reactive) |Resteasy Reactive, Hibernate Reactive, Smallrye Mutiny example.|
|[mutiny-hibernate-reactive](https://github.com/hantsy/quarkus-sample/tree/master/mutiny-hibernate-reactive) |Resteasy, Hibernate Reactive, Smallrye Mutiny example.|
|[vertx-routes-mutiny](https://github.com/hantsy/quarkus-sample/tree/master/vertx-routes-mutiny)  |Simple CRUD RESTful APIs using Vertx `Router` and Reactive PgClient with Mutiny APIs.|
|[multipart](https://github.com/hantsy/quarkus-sample/tree/master/multipart)  |Multipart examples|
|[amqp](https://github.com/hantsy/quarkus-sample/tree/master/amqp)  |AMQP, MP reactive messaging and JAXRS SSE examples|
|[GraphQL](https://github.com/hantsy/quarkus-sample/tree/master/graphql)  |GraphQL example using MP GraphQL/Smallrye GraphQL|
|[GraphQL Client](https://github.com/hantsy/quarkus-sample/tree/master/graphql-client)  |GraphQL Client example using MP GraphQL/Smallrye GraphQL|

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
