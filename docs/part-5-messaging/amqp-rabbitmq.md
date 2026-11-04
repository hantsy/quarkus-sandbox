# AMQP with RabbitMQ

The `amqp-rabbitmq` module uses the AMQP 1.0 connector to connect to
RabbitMQ. It keeps the AMQP connector configuration separate from the
RabbitMQ-specific SmallRye connector in the [`rabbitmq`](./rabbitmq.md)
module.

## Configure the AMQP channels

The module sends to and receives from the `messages` address:

```properties
mp.messaging.outgoing.send.connector=smallrye-amqp
mp.messaging.outgoing.send.address=messages
mp.messaging.outgoing.send.use-anonymous-sender=false
mp.messaging.incoming.messages.connector=smallrye-amqp
mp.messaging.incoming.messages.durable=false
```

The module's Compose file starts RabbitMQ with the `quarkus` development
account and labels the service for AMQP Dev Services discovery.

## Start RabbitMQ and the application

From the `amqp-rabbitmq` directory, start the broker:

```shell
docker compose up -d
```

Then start the Quarkus application in the same directory:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## Send and receive a message

The HTTP façade accepts `POST /messages` with a plain-text body and exposes
`GET /messages` as a JSON server-sent event stream. `MessageHandler` sends
through the outgoing `send` channel and transforms incoming strings into
timestamped messages.

## What's next

- Compare with the [AMQP connector](./amqp.md).
- See [RabbitMQ connector](./rabbitmq.md) for its native exchange and queue
  configuration.
