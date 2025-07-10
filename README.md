
# User Data gRPC Service (Java)

This project implements a simple gRPC server and client in Java for managing user data.

It includes:
- A gRPC server exposing two RPC methods: `GetUserData` and `GetOrCreateUser`
- A Java client that calls these RPCs
- Protobuf-based message definitions and service contract

## Features

- gRPC server (`UserServiceImpl`) with:
  - `GetUserData`: returns basic user data for a given `userId`
  - `GetOrCreateUser`: fetches or creates a user based on `userId` and `mobileNumber`
- gRPC client that:
  - Connects to the server
  - Sends requests and prints responses to console

## Technologies

- Java 17
- gRPC
- Protocol Buffers
- Maven

## Build

Generate the gRPC and protobuf sources and build the project:

```bash
mvn clean install
````

## Run the Server

Start the gRPC server:

```bash
java -cp target/test_grpc-1.0-SNAPSHOT.jar org.example.Server
```

The server starts listening on `localhost:50051`.

## Run the Client

Run the client to call the server:

```bash
java -cp target/test_grpc-1.0-SNAPSHOT.jar org.example.client.client
```

Sample output:

```
MobileNo:9876543210
isNewUser:true
```

## Notes

* The `.proto` file defines the service and message types, and is compiled via the `protobuf-maven-plugin`.
* The generated gRPC stubs and messages are located in `target/generated-sources/protobuf`.

## Project Structure

```
src/main/java/org/example/
├── Server.java                # gRPC server bootstrap
├── UserServiceImpl.java       # gRPC service implementation
└── client/client.java         # gRPC client
```

## References

* [gRPC Java documentation](https://grpc.io/docs/languages/java/)
* [Protocol Buffers](https://developers.google.com/protocol-buffers)

```


