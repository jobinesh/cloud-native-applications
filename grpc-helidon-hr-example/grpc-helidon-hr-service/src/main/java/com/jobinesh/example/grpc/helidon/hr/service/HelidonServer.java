package com.jobinesh.example.grpc.helidon.hr.service;

import io.helidon.microprofile.grpc.server.spi.GrpcMpContext;
import io.helidon.microprofile.grpc.server.spi.GrpcMpExtension;

public class HelidonServer implements GrpcMpExtension {
    @Override
    public void configure(GrpcMpContext grpcMpContext) {
        grpcMpContext.routing()
                .register(new HRServiceGrpcImpl());

    }
}
