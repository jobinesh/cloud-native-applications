package com.jobinesh.example.grpc.server;

import io.grpc.stub.StreamObserver;

public class HelloWorldServiceImpl extends HelloWorldServiceGrpc.HelloWorldServiceImplBase {
  @Override
  public void sayHello(HelloWorldServiceOuterClass.HelloWorldRequest request,
        StreamObserver<HelloWorldServiceOuterClass.HelloWorldResponse> responseObserver) {
  // HelloRequest has toString auto-generated.
    System.out.println(request);

    // You must use a builder to construct a new Protobuffer object
    HelloWorldServiceOuterClass.HelloWorldResponse response = HelloWorldServiceOuterClass.HelloWorldResponse.newBuilder()
      .setGreeting("Hello there, " + request.getName())
      .build();

    // Use responseObserver to send a single response back
    responseObserver.onNext(response);

    // When you are done, you must call onCompleted.
    responseObserver.onCompleted();
  }
}