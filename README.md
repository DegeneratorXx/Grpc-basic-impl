# User Data gRPC Service (Java)

This project implements a simple gRPC server and client in Java for managing user data stored in a PostgreSQL database.

It includes:
- A gRPC server exposing two RPC methods: `GetUserData` and `GetOrCreateUser`
- A Java client that calls these RPCs
- Protobuf-based message definitions and service contract
- PostgreSQL integration to persist user data

## Features

- gRPC server (`UserServiceImpl`) with:
  - `GetUserData`: returns mobile number for a given `userId`
  - `GetOrCreateUser`: fetches or creates a user based on `userId` and `mobileNumber`
- gRPC client that:
  - Connects to the server
  - Sends requests and prints responses to console
- PostgreSQL-backed persistence
  - Table `users` stores `user_id`, `mobile_number`, and `is_new_user` flag

## Technologies

- Java 17
- gRPC
- Protocol Buffers
- PostgreSQL
- Maven
- JDBC

## Notes

* The `.proto` file defines the service and message types, and is compiled via the `protobuf-maven-plugin`.
* The generated gRPC stubs and messages are located in `target/generated-sources/protobuf`.
* Ensure PostgreSQL is running and a table `users` exists in the database you connect to.

### Example table schema:
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    mobile_number VARCHAR(20),
    is_new_user BOOLEAN DEFAULT TRUE
);

```

## Notes

* The `.proto` file defines the service and message types, and is compiled via the `protobuf-maven-plugin`.
* The generated gRPC stubs and messages are located in `target/generated-sources/protobuf`.

## Project Structure

```
src/main/java/org/example/
├── Server.java                # gRPC server bootstrap
├── Service/UserServiceImpl.java   # gRPC service implementation
├── Repository/UserDataHelper.java # DB interaction helper
└── client/client.java         # gRPC client

```

## References

* [gRPC Java documentation](https://grpc.io/docs/languages/java/)
* [Protocol Buffers](https://developers.google.com/protocol-buffers)




