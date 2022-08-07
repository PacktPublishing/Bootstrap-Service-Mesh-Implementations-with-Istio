# Shopping Cart Service

The Shopping Cart Service is implemented as a multi-module project containing the following modules:

1. **[carts-core](./carts-core)** contains the bulk of the service implementation, including
domain model, the REST service itself, as well as the data repository abstraction and its 
in-memory implementation;

2. **[carts-coherence](./carts-coherence)** contains the data repository implementation for 
Coherence backend

3. **[carts-mongo](./carts-mongo)** contains the data repository implementation for MongoDB 
backend

4. **[carts-mysql](./carts-mysql)** contains the data repository implementation for MySQL 
backend (using JPA)

5. **[carts-redis](./carts-redis)** contains the data repository implementation for Redis 
backend (using Redisson client)

## API

The service exposes REST API on port 7001. 

TBD (add OpenAPI support/link)

## Building the service

In order to build all the modules and create Docker images for the service, simply run the 
following commands inside the top-level `carts` directory:

```bash
$ mvn clean install
$ mvn package -Pdocker -DskipTests
``` 

The first command will build all the modules, run unit and integration tests, and install the
artifacts that need to be included into the Docker images into the local Maven repo.

The second command will then package those artifacts, and all of their dependencies, into
the local Docker image with the same names as the corresponding module (one image per module).

You can then manually push generated image to a Docker repository of your choice in order
to make it available to other environments.

Alternatively, you can build and push the image directly to a remote Docker repository by
running the following command instead:

```bash
$ mvn package -Pdocker -DskipTests -Ddocker.repo=<your_docker_repo> -Djib.goal=build
```

You should replace `<your_docker_repo>` in the command above with the name of the 
Docker repository that you can push images to.

## Running the service

Please refer to documentation for the individual modules above to find out how to run
different implementations of this service, including the backends they require, for local
testing.

To learn how to run the service in Kubernetes, as part of a larger Sock Shop application,
please refer to the [main documentation page](../sockshop/README.md).

## License

The Universal Permissive License (UPL), Version 1.0