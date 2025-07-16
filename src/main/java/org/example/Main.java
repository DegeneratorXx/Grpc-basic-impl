package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import org.example.Config.OtelConfig;
import org.example.Service.UserServiceImpl;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        OtelConfig.init();

        Server server = ServerBuilder
                .forPort(50051)
                .addService(new UserServiceImpl())
                .addService(ProtoReflectionService.newInstance())
                .build();

        System.out.println("gRPC server started on port 50051");
        server.start();
        server.awaitTermination();
    }
}