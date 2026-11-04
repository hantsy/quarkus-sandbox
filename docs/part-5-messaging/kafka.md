# Kafka with Reactive Messaging

The `kafka` module uses SmallRye Reactive Messaging with Kafka. The outgoing
channel is named `send`; the incoming `messages` channel consumes from the
same-named Kafka topic.

## Configure the Kafka channels

The module's settings include:

```properties
%prod.kafka.bootstrap.servers=kafka:9092
mp.messaging.outgoing.send.connector=smallrye-kafka
mp.messaging.incoming.messages.connector=smallrye-kafka
mp.messaging.incoming.messages.topic=send
mp.messaging.incoming.messages.group.id=${quarkus.uuid}
```

The sample enables earliest offset reset and automatic topic creation. The
bootstrap server override is active in the `prod` profile. Check the
development configuration and broker setup for the profile you run.

## Follow the message flow

The HTTP resource sends plain text to the outgoing `send` channel. The
`messages` channel consumes the `send` topic, and the handler publishes
timestamped messages to `messages-stream`. `GET /messages` exposes the stream
as server-sent events.

## Run the sample

From the `kafka` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. The POM and Quarkus Dev Services
configuration provide the broker setup options; choose the provider in the
module configuration if needed.

## What's next

- Compare [Pulsar](./pulsar.md).
- See [AMQP with RabbitMQ](./amqp-rabbitmq.md).
