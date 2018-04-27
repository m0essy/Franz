# Franz

This project lets you have fun with the blockchain. It is a small scala wrapper around [multichain](https://www.multichain.com/).

It uses akka-http client to provide an asynchronous com.snekyx.franz.api to use Multichain commands, and circe to encode and decode Json.

At the moment the following Multichain commands are supported:

Addresses:
- create new address

Permissions:

Streams:
- listStreams
- ceateStream
- createStreamFrom
- subscribe

Transactions:

more are hopefully available soon

### next steps:

- add proper error handling
- add missing stream commands (subscribe, publish, etc.)
- add logging
- add examples
- add documentation

also hopefully coming soon...
Akka stream api for Multichain streams

### examples
