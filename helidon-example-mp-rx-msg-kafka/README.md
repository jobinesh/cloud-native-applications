# Helidon Kafka Reactive Messaging MP Example

This example demonstrates the integration of Kafka with a simple Microservice built using Helidon MicroProfile.

## Install All Required Tools
- Install Docker: https://docs.docker.com/install/
- Install Docker-compose: https://docs.docker.com/compose/install/

## Clone the Repository
- Clone the git https://github.com/jobinesh/cloud-native-applications.git
- Navigate to helidon-example-mp-rx-msg-kafka

## Start Kafka
We need a Kafka cluster to run this example. Please finish this step before proceeding further. 
Go to project root folder in the project source and run the docker-compose up as shown here. This is the quick way of getting kafka running locally: 
 ``` 
  docker-compose up -d
 ```
Alternatively you can set up Kafka locally by following the instructions from Apache Kafka web site: https://kafka.apache.org/quickstart

## Build and Run

With JDK8+
```bash
mvn package
java -jar target/helidon-example-mp-rx-msg-kafka
```
## A Glance at the Implementation
- RxMessagingKafkaConsumer: This class listens to the Kafka topic using the MicroProfile Reactive Messaging declarative constructs. 
- RxMessagingKafkaProducer:  This class publishes messages to the Kafka topic using the MicroProfile Reactive Messaging declarative constructs. It is used by GreetResource class.
- GreetResource: This is the REST resource implementation used in this example. When updateGreeting method is called
via REST PUT operation, the new greeting sent by client is sent to Kafka queue for consumption by the Kafka consumers
- microprofile-config.properties: This config file holds the kafka configuration used in this example

## Exercising the Example

This is simple example that use Kafka to notify intrested parties whenevr greeting resource is modified.
Whenever a client modifies the greeting via PUT method call as shown below, the API implementation
publishes the modified greeting value to kafka topic. Kafka consumers who listen on the topic, 
gets the modified greeting  messages. 

```
curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8090/greet/greeting

```
## Stop and Remove the Containers Used for this Example
```
docker-compose stop 
docker-compose rm
```
