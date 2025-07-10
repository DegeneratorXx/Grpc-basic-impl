package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class client {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(client.class);
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
        logger.info("response for GetUserData");
        logger.info("mobile numner rxd:"+ response.getMobileNumber());

        GetOrCreateUserResponse response2= stub.getOrCreateUser(request2);
        logger.info("response for GetOrCreateUserResponse");
        logger.info("isNewUser:" + response2.getIsNewUser());

        managedChannel.shutdown();

    }

}
