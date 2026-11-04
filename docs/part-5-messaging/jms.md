# Jakarta Messaging with Qpid JMS

The `jms` module uses Jakarta Messaging (JMS) APIs with Quarkus Qpid JMS.
Unlike the other messaging modules, it creates and manages a JMS context,
queue, producer, and consumer directly rather than using Reactive Messaging.

## Configure the JMS connection

The module sets the Qpid JMS URL and username in `application.properties`.
Its local Compose file starts an Artemis broker that listens on port `5672`.
Review the local broker credentials when configuring your environment.

[`JmsResources`](https://github.com/hantsy/quarkus-sandbox/blob/master/jms/src/main/java/com/example/JmsResources.java)
produces a `JMSContext` from the connection factory and creates the
`HELLO_QUEUE` queue.

## Send and receive messages

[`MessageProducer`](https://github.com/hantsy/quarkus-sandbox/blob/master/jms/src/main/java/com/example/MessageProducer.java)
serializes timestamped messages with JSON-B and sends them to the JMS queue.
[`MessageConsumer`](https://github.com/hantsy/quarkus-sandbox/blob/master/jms/src/main/java/com/example/MessageConsumer.java)
consumes messages asynchronously, deserializes them, and publishes them to a
Mutiny stream. The HTTP resource accepts `POST /messages` and exposes
`GET /messages` as server-sent events.

## Start Artemis and the application

From the `jms` directory, start Artemis:

```shell
docker compose up -d
```

Then run the application:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- Compare Reactive Messaging channels in [AMQP](./amqp.md).
- See [Pulsar](./pulsar.md) and [Kafka](./kafka.md) for their connectors.
