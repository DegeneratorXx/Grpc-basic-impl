package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.generated.*;

public class client {
    public static void main(String[] args) {

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost",50051)
                .usePlaintext().build();

        UserServiceGrpc.UserServiceBlockingStub stub= UserServiceGrpc.newBlockingStub(managedChannel);


        GetUserDataRequest request = GetUserDataRequest.newBuilder()
                .setUserId(123).build();

        GetOrCreateUserRequest request2= GetOrCreateUserRequest.newBuilder()
                .setUserId(123)
                .setMobileNumber("9876543210")
                .build();


        GetUserDataResponse response= stub.getUserData(request);
        System.out.println("MobileNo:" + response.getMobileNumber());

        GetOrCreateUserResponse response2= stub.getOrCreateUser(request2);
        System.out.println("isNewUser: "+ response2.getIsNewUser());

        managedChannel.shutdown();

    }

}
