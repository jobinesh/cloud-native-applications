# Helidon  MP Jaeger Example

This example demonstrates how Jaeger can be used for tracing in a Helidon MP application 

# Start the Jaeger via Docker
docker run --rm -it --name jaeger \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  jaegertracing/all-in-one:1.14  
  
## Build and run the application

With JDK8+
```bash
mvn package
java -jar target/helidon-example-mp-jaeger.jar
```

## Exercise the application

```
curl -X GET http://localhost:8080/greet
{"message":"Hello World!"}

curl -X GET http://localhost:8080/greet/Joe
{"message":"Hello Joe!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8080/greet/greeting

curl -X GET http://localhost:8080/greet/Jose
{"message":"Hola Jose!"}
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

## View the tracing with Jaegar

Now, in order to view the API call tracing for the above invocations with Jaeger, open a browser and navigate to the following URL:
http://localhost:16686/search

## Build the Docker Image

```
docker build -t helidon-example-mp-jaeger .
```

## Start the application with Docker

```
docker run --rm -p 8080:8080 helidon-example-mp-jaeger:latest
```

Exercise the application as described above

## Deploy the application to Kubernetes

```
kubectl cluster-info                         # Verify which cluster
kubectl get pods                             # Verify connectivity to cluster
kubectl create -f app.yaml               # Deploy application
kubectl get service helidon-example-mp-jaeger  # Verify deployed service
```
