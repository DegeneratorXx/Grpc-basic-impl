package org.example;

import org.example.generated.*;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUserData(GetUserDataRequest request, StreamObserver<GetUserDataResponse> responseObserver) {
        GetUserDataResponse response = GetUserDataResponse.newBuilder()
                .setMobileNumber("9876543210")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrCreateUser(GetOrCreateUserRequest request, StreamObserver<GetOrCreateUserResponse> responseObserver) {
        GetOrCreateUserResponse response = GetOrCreateUserResponse.newBuilder()
                .setIsNewUser(false)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
