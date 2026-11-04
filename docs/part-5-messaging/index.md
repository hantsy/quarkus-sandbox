# Part 5: Messaging

Each messaging recipe uses one connector or messaging API. Most samples use
the same HTTP façade: `POST /messages` sends a text message, and
`GET /messages` streams received messages as JSON server-sent events. The JMS
sample uses Jakarta Messaging directly; the other samples use SmallRye
Reactive Messaging.

Choose the recipe that matches the connector and broker you want to explore:

- [Reactive Messaging with AMQP](./amqp.md)
- [AMQP with RabbitMQ](./amqp-rabbitmq.md)
- [Reactive Messaging with RabbitMQ](./rabbitmq.md)
- [JMS with Qpid JMS](./jms.md)
- [Reactive Messaging with Pulsar](./pulsar.md)
- [Reactive Messaging with Kafka](./kafka.md)

These modules are separate applications with separate broker settings. Do not
assume one broker URL or Compose file works for another module.

## What's next

- See [Miscellaneous examples](../part-6-misc/index.md).
- See [REST clients](../part-3-rest-clients/index.md) for HTTP integrations.
