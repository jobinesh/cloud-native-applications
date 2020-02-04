package com.jobinesh.example.grpc.helidon.hr.client;


//import com.jobinesh.example.grpc.helidon.hr.api.StringService;
import io.helidon.microprofile.grpc.client.GrpcChannel;
import io.helidon.microprofile.grpc.client.GrpcServiceProxy;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.inject.Inject;
/**
 * A client to the {@link StringService}.
 * <p>
 * This client is a CDI bean which will be initialised when the CDI container
 * is initialised in the {@link #main(String[])} method.
 */
@ApplicationScoped
public class Client {

    /**
     * The {@link StringService} client to use to call methods on the server.
     * <p>
     * A dynamic proxy of the {@link StringService} interface will be injected by CDI.
     * This proxy will connect to the service using the default {@link io.grpc.Channel}.
     */
    @Inject
    @GrpcServiceProxy
    @GrpcChannel(name = "test-server")
    private StringService stringService;


    /**
     * Program entry point.
     *
     * @param args  the program arguments
     *
     * @throws Exception if an error occurs
     */
    public static void main(String[] args) throws Exception {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        SeContainer container = initializer.initialize();

        Client client = container.select(Client.class).get();

        client.unary();
    }

    /**
     * Call the unary {@code Lower} method.
     */
    public void unary() {
        String response = stringService.upper("abcd");
        System.out.println("Unary Lower response: '" + response + "'");
    }

}
