package com.jobinesh.example.grpc.helidon.hr.client;


import io.helidon.microprofile.grpc.core.RpcService;

/**
 * The gRPC StringService.
 * <p>
 * This class has the {@link RpcService} annotation
 * so that it will be discovered and loaded using CDI when the MP gRPC server starts.
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
public interface StringService {

    /**
     * Convert a string value to upper case.
     *
     * @param request  the request containing the string to convert
     * @return the request value converted to upper case
     */
    String upper(String request);

}
