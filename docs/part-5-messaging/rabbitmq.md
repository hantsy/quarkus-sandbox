# Reactive Messaging with RabbitMQ

The `rabbitmq` module uses the SmallRye Reactive Messaging RabbitMQ connector.
It configures a named exchange and queue rather than using the AMQP 1.0
connector.

## Configure the exchange and queue

The outgoing channel publishes to the `messages` exchange. The incoming
channel reads from the `messages` queue and exchange:

```properties
mp.messaging.outgoing.send.connector=smallrye-rabbitmq
mp.messaging.outgoing.send.exchange.name=messages
mp.messaging.incoming.messages.connector=smallrye-rabbitmq
mp.messaging.incoming.messages.queue.name=messages
mp.messaging.incoming.messages.exchange.name=messages
```

The module's configuration references RabbitMQ Dev Services. Its host and
credentials are commented examples, so check the local broker configuration
before starting the application.

## Run the sample

Start a RabbitMQ broker using Dev Services or your local environment. From the
`rabbitmq` directory, run:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

The HTTP interface accepts `POST /messages` and streams received messages
from `GET /messages` as server-sent events.

## What's next

- Compare AMQP 1.0 connectivity in [AMQP with RabbitMQ](./amqp-rabbitmq.md).
- See [Kafka](./kafka.md) for a partitioned log connector.
