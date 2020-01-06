# Helidon Quickstart MP Example

This example demonstrates the integration of Kafka with a simple Microservice built using Helidon MicroProfile.

## Start Kafka
We need a Kafka cluster to run this example. Please finish this step before proceeding further. 
You can follow either of the following to run the Kafka locally
- Set up Kafka locally by following the instructions from Apache Kafka web site: https://kafka.apache.org/quickstart
- Alternatively  you can run Kafka docker image, which may help you to get Kafka up and running in no time. 
Go to kafka-docker folder in the project source and run the docker-compose up as shown here: 
 ``` 
  cd kafka-docker
  docker-compose up
 ```

## Build and run

With JDK8+
```bash
mvn package
java -jar target/helidon-example-mp-kafka.jar
```

## Exercise the application

This is simple example that use Kafka to notify intrested parties whenevr greeting resource is modified.
Whenever a client modifies the greeting via PUT method call as shown below, the API implementation
publishes the modified greeting value to kafka topic. Kafka consumers who listen on the topic, 
gets the modified greeting  messages. 

```
curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8090/greet/greeting

```
