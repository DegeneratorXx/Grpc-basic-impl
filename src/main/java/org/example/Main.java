package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.example.Service.UserServiceImpl;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder
                .forPort(50051)
                .addService(new UserServiceImpl())
                .addService(ProtoReflectionService.newInstance())  // 👈 Add this
                .build();

        System.out.println("gRPC server started on port 50051");
        server.start();
        server.awaitTermination();
    }
}