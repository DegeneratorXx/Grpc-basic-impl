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
        System.out.println(getUserResponse.getMobileNumber());


        // Request 2: Create or check a user
        GetOrCreateUserRequest createOrCheckRequest = GetOrCreateUserRequest.newBuilder()
                .setUserId(1)
                .setMobileNumber("9876543210")
                .build();

        GetOrCreateUserResponse createOrCheckResponse = stub.getOrCreateUser(createOrCheckRequest);
        System.out.println("Is New User: " + createOrCheckResponse.getIsNewUser());

        // Request 3: Get Details of non existing user
        GetUserDataRequest getUserDataRequest = GetUserDataRequest.newBuilder()
                .setUserId(5)
                .build();

        GetUserDataResponse getUserDataResponse = stub.getUserData(getUserDataRequest);
        System.out.println(getUserDataResponse.getMobileNumber());

        // Request 4: Create or check a user
        GetOrCreateUserRequest createOrCheckRequest2 = GetOrCreateUserRequest.newBuilder()
                .setUserId(11)
                .setMobileNumber("9876543210")
                .build();

        GetOrCreateUserResponse createOrCheckResponse2= stub.getOrCreateUser(createOrCheckRequest2);
        System.out.println("Is New User: " + createOrCheckResponse2.getIsNewUser());

        managedChannel.shutdown();
    }
}
