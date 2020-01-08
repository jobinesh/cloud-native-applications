# Helidon Quickstart MP Example

This example demonstrates the integration of Kafka with a simple Microservice built using Helidon MicroProfile.

## Install All Required Tools
Install Docker: https://docs.docker.com/install/
Install Docker-compose: https://docs.docker.com/compose/install/

Clone the git https://github.com/jobinesh/cloud-native-applications.git

## Start Kafka
We need a Kafka cluster to run this example. Please finish this step before proceeding further. 
Go to project root folder in the project source and run the docker-compose up as shown here. This is the quick way of getting kafka running locally: 
 ``` 
  docker-compose up -d
 ```
Alternatively you can set up Kafka locally by following the instructions from Apache Kafka web site: https://kafka.apache.org/quickstart

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
