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
## Observability

### Auto Instrumentation :

You can download Java Agent for no-code oTel implementation from here: [oTel Java Agent](https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar)  
To process the collected telemetry data download Jaeger: [Jaeger](https://www.jaegertracing.io/download/)

Implemented oTel using no-code Java Agent:  
Basic workflow:

* gRPC server started.
* Java Agent jar connected to it when running, which is configured to send telemetry data to port 4317.
* The agent does automatic instrumentation, without touching our code it creates spans for gRPC calls coming and going and when SQL queries are executed, and sends these spans to the configured endpoint via OTLP gRPC.
* Telemetry data is sent to the OTLP collector using OTLP gRPC.
* Jaeger, which also has an OTLP collector embedded in it, receives the telemetry data.
* We can see our service in Jaeger’s UI on the localhost which is `http://localhost:16686` by default.

### To run Jaeger:
```
./jaeger
```

### To run your gRPC server with oTel Java Agent:
````
java \
  -javaagent:path-to-agent-jar.jar \
  -Dotel.service.name=your-service-name \
  -Dotel.exporter.otlp.protocol=grpc \
  -Dotel.exporter.otlp.endpoint=http://127.0.0.1:4317 \
  -Dotel.metrics.exporter=none \
  -jar target/your-app.jar
````
### To see the metrics:
integrated an OpenTelemetry Collector, which receives telemetry data from my app,
exposes metrics in Prometheus format, and Prometheus scrapes those metrics. 
Finally, Grafana visualizes the Prometheus data.

Prometheus and oTel collector's yaml files are added in the repo itself which specifies
properties of them.

To run the oTel collector:
```
./otelcol-contrib --config otel-collector-config.yaml
```
To run prometheus:
````
 path/to/prometheus-3.5.0.darwin-amd64/prometheus \
  --config.file=path/to/prometheus.yml
````


## References

* [gRPC Java documentation](https://grpc.io/docs/languages/java/)
* [Protocol Buffers](https://developers.google.com/protocol-buffers)
* [OpenTelemetry Documentation](https://opentelemetry.io/docs/)
* [Otel Collector](https://github.com/open-telemetry/opentelemetry-collector-releases/releases?q=otel-contrib&expanded=true)
* [Prometheus Docs](https://prometheus.io/docs/introduction/overview/)



