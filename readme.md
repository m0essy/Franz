# Franz

This project lets you have fun with the blockchain. It is a small scala wrapper around [multichain](https://www.multichain.com/).

It uses akka-http client to provide an asynchronous com.snekyx.franz.api to use Multichain commands, and circe to encode and decode Json.

At the moment the following Multichain commands are supported:

- listStream
- createStreamFrom

more are hopefully avaliable soon

###next steps:

- add tests
- add missing stream commands (subscribe, publish, etc.)
- add logging
- add examples
- add documentation

also hopefully coming soon...