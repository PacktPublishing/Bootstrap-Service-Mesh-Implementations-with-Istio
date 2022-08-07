# carts-core

This module contains the bulk of the Shopping Cart service implementation, including the 
[domain model](./src/main/java/io/helidon/examples/sockshop/carts/Cart.java), 
[REST API](./src/main/java/io/helidon/examples/sockshop/carts/CartResource.java), as well as the
[data repository abstraction](./src/main/java/io/helidon/examples/sockshop/carts/CartRepository.java) 
and its [in-memory implementation](./src/main/java/io/helidon/examples/sockshop/carts/DefaultCartRepository.java).

## Building the Service

See [main documentation page](../README.md#building-the-service) for instructions.

## Running the Service

Because this implementation uses in-memory data store, it is trivial to run.

Once you've built the Docker image per instructions above, you can simply run it by executing:

```bash
$ docker run -p 7001:7001 ghcr.io/helidon-sockshop/carts-core
``` 

Once the container is up and running, you should be able to access [service API](../README.md#api) 
by navigating to http://localhost:7001/carts/.

As a basic test, you should be able to perform an HTTP GET against `/carts/{customerId}` endpoint:

```bash
$ curl http://localhost:7001/carts/123
``` 
which should return JSON response
```json
{
  "customerId": "123"
}
```

## License

The Universal Permissive License (UPL), Version 1.0
