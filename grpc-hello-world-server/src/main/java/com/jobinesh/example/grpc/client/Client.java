package com.jobinesh.example.grpc.client;

import io.grpc.*;
import com.jobinesh.example.grpc.server.*;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
      // Channel is the abstraction to connect to a service endpoint
      // Let's use plaintext communication because we don't have certs
      final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
        .usePlaintext(true)
        .build();

      // It is up to the client to determine whether to block the call
      // Here we create a blocking stub, but an async stub,
      // or an async stub with Future are always possible.
      HelloWorldServiceGrpc.HelloWorldServiceBlockingStub stub = HelloWorldServiceGrpc.newBlockingStub(channel);
      HelloWorldServiceOuterClass.HelloWorldRequest request =
        HelloWorldServiceOuterClass.HelloWorldRequest.newBuilder()
          .setName("Jobinesh")
          .build();

      // Finally, make the call using the stub
      HelloWorldServiceOuterClass.HelloWorldResponse response = 
        stub.sayHello(request);

      System.out.println(response);

      // A Channel should be shutdown before stopping the process.
      channel.shutdownNow();
    }
}