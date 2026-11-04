# Reactive Messaging with Pulsar

The `pulsar` module connects SmallRye Reactive Messaging channels to Apache
Pulsar. Its outgoing channel publishes to the `messages` topic; the incoming
channel consumes from Pulsar.

## Configure the topic

The sample uses these channel settings:

```properties
%prod.pulsar.client.serviceUrl=pulsar://pulsar:6650
mp.messaging.outgoing.send.connector=smallrye-pulsar
mp.messaging.outgoing.send.topic=messages
mp.messaging.incoming.messages.connector=smallrye-pulsar
mp.messaging.incoming.messages.subscriptionInitialPosition=Earliest
```

The Pulsar service URL is set for the `prod` profile. Configure the broker
host and service URL appropriate to your environment before using another
profile.

## Send and stream a message

The HTTP façade accepts a text message at `POST /messages`. The outgoing
`send` channel publishes it to Pulsar. The incoming `messages` channel
transforms consumed text into a timestamped message, which `GET /messages`
streams as JSON server-sent events.

## Run the sample

Start a Pulsar broker reachable at the configured service URL. Then run the
application from the `pulsar` directory:

```shell
./mvnw quarkus:dev
```

On Windows, run `mvnw.cmd quarkus:dev`.

## What's next

- Compare [Reactive Messaging with Kafka](./kafka.md).
- See [AMQP](./amqp.md) for an AMQP broker connector.
