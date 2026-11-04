# Send and receive messages

The messaging modules show how to connect Quarkus applications to AMQP,
RabbitMQ, JMS, Pulsar, and Kafka. Most samples use Reactive Messaging; the JMS
sample uses the Jakarta Messaging API directly.

The broker-specific projects are separate. Check a module's dependencies,
`application.properties`, and Compose file before choosing a broker or
copying its configuration.

## Choose a connector

| Sample | Messaging API | What the sample configures |
|---|---|---|
| [`amqp`](https://github.com/hantsy/quarkus-sandbox/tree/master/amqp) | SmallRye Reactive Messaging AMQP | Sends to the `messages` address and receives from an AMQP channel |
| [`amqp-rabbitmq`](https://github.com/hantsy/quarkus-sandbox/tree/master/amqp-rabbitmq) | AMQP 1.0 connector with RabbitMQ | Uses the AMQP connector; the module includes a RabbitMQ Compose file |
| [`rabbitmq`](https://github.com/hantsy/quarkus-sandbox/tree/master/rabbitmq) | SmallRye RabbitMQ connector | Configures a RabbitMQ exchange and queue |
| [`jms`](https://github.com/hantsy/quarkus-sandbox/tree/master/jms) | Jakarta Messaging with Qpid JMS | Sends and consumes JSON messages through a JMS queue |
| [`pulsar`](https://github.com/hantsy/quarkus-sandbox/tree/master/pulsar) | SmallRye Pulsar connector | Sends and receives on the `messages` topic |
| [`kafka`](https://github.com/hantsy/quarkus-sandbox/tree/master/kafka) | SmallRye Kafka connector | Connects the outgoing `send` channel to the incoming `messages` channel |

`amqp-rabbitmq` uses the AMQP connector to communicate with RabbitMQ. The
separate `rabbitmq` module uses the RabbitMQ connector and its exchange and
queue configuration.

## Understand the message flow

The Reactive Messaging modules share a small HTTP interface:

- `POST /messages` accepts a plain-text message.
- The resource sends the message to the broker through the `send` channel.
- The `messages` incoming channel consumes it and publishes a timestamped
  message to `messages-stream`.
- `GET /messages` exposes that stream as JSON server-sent events.

The JMS module exposes the same HTTP paths but uses a JMS producer and
consumer. Its consumer converts JSON into a `Message` and emits it to the
stream.

See the
[`amqp/MessageHandler`](https://github.com/hantsy/quarkus-sandbox/blob/master/amqp/src/main/java/com/example/MessageHandler.java),
[`amqp/MessageResource`](https://github.com/hantsy/quarkus-sandbox/blob/master/amqp/src/main/java/com/example/MessageResource.java),
and
[`jms/MessageConsumer`](https://github.com/hantsy/quarkus-sandbox/blob/master/jms/src/main/java/com/example/MessageConsumer.java)
for the implementation.

## Start the sample and broker

Start the broker configured by the module, then run its Quarkus application.
For example, the `amqp-rabbitmq` module has a Compose file that starts
RabbitMQ. From that module's directory, run:

```shell
docker compose up -d
```

In another terminal, start the application from the same directory:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The `jms` module also includes an
Artemis Compose file. The root Compose file defines an Artemis service for the
AMQP sample. Check the broker URL and credentials in each module's
`application.properties` before sending messages.

## What's next

- See [Other examples](./other-examples.md) for scheduling and multipart
  examples.
- See [REST clients](./rest-clients.md) for outbound HTTP communication.
