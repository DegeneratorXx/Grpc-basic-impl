package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.generated.*;

public class client {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(managedChannel);

        // Request 1: Get details of an existing user
        GetUserDataRequest getUserRequest = GetUserDataRequest.newBuilder()
                .setUserId(2)
                .build();

        GetUserDataResponse getUserResponse = stub.getUserData(getUserRequest);
        System.out.println("Existing User Mobile Number: " + getUserResponse.getMobileNumber());


        // Request 2: Create or check a user
        GetOrCreateUserRequest createOrCheckRequest = GetOrCreateUserRequest.newBuilder()
                .setUserId(456)
                .setMobileNumber("9876543210")
                .build();

        GetOrCreateUserResponse createOrCheckResponse = stub.getOrCreateUser(createOrCheckRequest);
        System.out.println("Is New User: " + createOrCheckResponse.getIsNewUser());

        managedChannel.shutdown();
    }
}
