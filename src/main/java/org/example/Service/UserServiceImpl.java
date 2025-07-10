package org.example.Service;

import org.example.Repository.UserDataHelper;
import org.example.generated.*;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUserData(GetUserDataRequest request, StreamObserver<GetUserDataResponse> responseObserver) {
        try {
            UserDataHelper helper = new UserDataHelper();
            String mobileNumber = helper.getMobileNumberByUserId(request.getUserId());

            if (mobileNumber == null) {
                mobileNumber = "";  // or "UNKNOWN"
            }

            GetUserDataResponse response = GetUserDataResponse.newBuilder()
                    .setMobileNumber(mobileNumber)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }
    }




    @Override
    public void getOrCreateUser(GetOrCreateUserRequest request, StreamObserver<GetOrCreateUserResponse> responseObserver) {
        UserDataHelper helper = new UserDataHelper();
        String mobileNumber = request.getMobileNumber();

        if(mobileNumber==null)
            mobileNumber="";

        boolean isNewUser=helper.getOrCreateAndCheckIsNew(request.getUserId(),mobileNumber);
        GetOrCreateUserResponse response = GetOrCreateUserResponse.newBuilder()
                .setIsNewUser(isNewUser)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
