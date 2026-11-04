# Reactive Messaging with AMQP

The `amqp` module connects SmallRye Reactive Messaging channels to an AMQP
broker. It sends strings to the `messages` address, consumes the incoming
channel, and streams timestamped messages over HTTP.

## Configure channels

The module uses `smallrye-amqp` for both channels:

```properties
mp.messaging.outgoing.send.connector=smallrye-amqp
mp.messaging.outgoing.send.address=messages
mp.messaging.incoming.messages.connector=smallrye-amqp
mp.messaging.incoming.messages.durable=true
```

The module's `application.properties` also declares the AMQP development
credentials. Keep credentials in local configuration rather than copying them
into application code.

## Follow the message flow

[`MessageHandler`](https://github.com/hantsy/quarkus-sandbox/blob/master/amqp/src/main/java/com/example/MessageHandler.java)
sends to the `send` channel with an `Emitter<String>`. Its `@Incoming("messages")`
method transforms a consumed string into a timestamped `Message` and emits it
to `messages-stream`. The HTTP resource accepts `POST /messages` and exposes
`GET /messages` as server-sent events.

## Run the sample

Use the AMQP broker configuration available in Dev Services or the root
Compose file. From the `amqp` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`. Check the application logs and
`MessageResource` to confirm the broker-to-stream flow.

## What's next

- Compare the AMQP transport to RabbitMQ in [AMQP with RabbitMQ](./amqp-rabbitmq.md).
- See the RabbitMQ-native connector in [RabbitMQ](./rabbitmq.md).
