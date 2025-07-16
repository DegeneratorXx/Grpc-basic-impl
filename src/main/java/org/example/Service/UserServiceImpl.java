package org.example.Service;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.example.Repository.UserDataHelper;
import org.example.generated.*;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    Tracer tracer = GlobalOpenTelemetry.getTracer("grpc-server");

    @Override
    public void getUserData(GetUserDataRequest request, StreamObserver<GetUserDataResponse> responseObserver) {
        Span span = tracer.spanBuilder("getUserData").startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("user-id", request.getUserId());

            UserDataHelper helper = new UserDataHelper();
            String mobileNumber = helper.getMobileNumberByUserId(request.getUserId());

            if (mobileNumber == null) {
                mobileNumber = ""; // or "UNKNOWN"
            }

            GetUserDataResponse response = GetUserDataResponse.newBuilder()
                    .setMobileNumber(mobileNumber)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            span.recordException(e);
            e.printStackTrace();
            responseObserver.onError(e);
        } finally {
            span.end();
        }
    }

    @Override
    public void getOrCreateUser(GetOrCreateUserRequest request, StreamObserver<GetOrCreateUserResponse> responseObserver) {
        Span span = tracer.spanBuilder("getOrCreateUser").startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("user-id", request.getUserId());

            UserDataHelper helper = new UserDataHelper();
            String mobileNumber = request.getMobileNumber();
            if (mobileNumber == null) {
                mobileNumber = "";
            }

            boolean isNewUser = helper.getOrCreateAndCheckIsNew(request.getUserId(), mobileNumber);

            GetOrCreateUserResponse response = GetOrCreateUserResponse.newBuilder()
                    .setIsNewUser(isNewUser)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            span.recordException(e);
            e.printStackTrace();
            responseObserver.onError(e);
        } finally {
            span.end();
        }
    }
}
