package com.jobinesh.example.grpc.helidon.hr.service;


import io.helidon.config.Config;
import io.helidon.grpc.server.GrpcRouting;
import io.helidon.grpc.server.GrpcServer;
import io.helidon.grpc.server.GrpcServerConfiguration;
import io.helidon.health.HealthSupport;
import io.helidon.health.checks.HealthChecks;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerConfiguration;
import io.helidon.webserver.WebServer;

import java.util.logging.LogManager;

/**
 * A basic example of a Helidon gRPC server.
 */
public class HelidonSEServer {

    private HelidonSEServer() {
    }

    /**
     * The main program entry point.
     *
     * @param args  the program arguments
     *
     * @throws Exception  if an error occurs
     */
    public static void main(String[] args) throws Exception {
        System.out.println("HelidonSEServer is getting ready !");
        // By default this will pick up application.yaml from the classpath
        Config config = Config.create();

        // load logging configuration
        LogManager.getLogManager().readConfiguration(
                HelidonSEServer.class.getResourceAsStream("/logging.properties"));

        // Get gRPC server config from the "grpc" section of application.yaml
        GrpcServerConfiguration serverConfig =
                GrpcServerConfiguration.builder(config.get("grpc")).build();

        GrpcServer grpcServer = GrpcServer.create(serverConfig, createRouting(config));

        // Try to start the server. If successful, print some info and arrange to
        // print a message at shutdown. If unsuccessful, print the exception.
        grpcServer.start()
                .thenAccept(s -> {
                    System.out.println("gRPC server is UP! http://localhost:" + s.port());
                    s.whenShutdown().thenRun(() -> System.out.println("gRPC server is DOWN. Good bye!"));
                })
                .exceptionally(t -> {
                    System.err.println("Startup failed: " + t.getMessage());
                    t.printStackTrace(System.err);
                    return null;
                });

        // add support for standard and gRPC health checks
        HealthSupport health = HealthSupport.builder()
                .add(HealthChecks.healthChecks())
                .add(grpcServer.healthChecks())
                .build();

        // start web server with health endpoint
        Routing routing = Routing.builder()
                .register(health)
                .build();

        ServerConfiguration webServerConfig = ServerConfiguration.builder(config.get("webserver")).build();

        WebServer.create(webServerConfig, routing)
                .start()
                .thenAccept(s -> {
                    System.out.println("HTTP server is UP! http://localhost:" + s.port());
                    s.whenShutdown().thenRun(() -> System.out.println("HTTP server is DOWN. Good bye!"));
                })
                .exceptionally(t -> {
                    System.err.println("Startup failed: " + t.getMessage());
                    t.printStackTrace(System.err);
                    return null;
                });
        System.out.println("HelidonSEServer is ready !");
    }

    private static GrpcRouting createRouting(Config config) {

        return GrpcRouting.builder()
                .register(new HRServiceGrpcImpl())
                .build();
    }
}
