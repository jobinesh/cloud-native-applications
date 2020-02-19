# gRPC Helidon HR Service

This example implements a simple Hello World REST service using MicroProfile.

## Build and run

With JDK8+
```bash
mvn package
mvn exec:java
```

## Exercise the application
```
 $ cd  ../grpc-helidon-hr-client  
 $ mvn install
 $ mvn exec:java -DskipTests -Dexec.mainClass=com.jobinesh.example.grpc.helidon.hr.client.GrpcClient
```

## Try health and metrics

```
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...
. . .

# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .

```

## Build the Docker Image

```
docker build -t grpc-helidon-hr-service .
```

## Start the application with Docker

```
docker run --rm -p 8080:8080 grpc-helidon-hr-service:latest
```

Exercise the application as described above

## Deploy the application to Kubernetes

```
kubectl cluster-info                         # Verify which cluster
kubectl get pods                             # Verify connectivity to cluster
kubectl create -f app.yaml               # Deploy application
kubectl get service grpc-helidon-hr-service  # Verify deployed service
```
