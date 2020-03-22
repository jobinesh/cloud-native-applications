# Helidon MP + Redis Publish SubScribe Example

This example demonstrates the integration of Redis with a simple Microservice built using Helidon MicroProfile.

## Install All Required Tools
- Install Docker: https://docs.docker.com/install/

## Clone the Repository
- Clone the git https://github.com/jobinesh/cloud-native-applications.git
- Navigate to helidon-example-mp-redis

## Start Redis
We can use th Redis docker image for quickly setting up the env
 ``` 
$ docker run -d -p 6379:6379 --name redis-demo redis
 ```
Alternatively you can set up Redis locally by following the instructions from the web site: https://redis.io/download

## Build and Run

With JDK8+
```bash
mvn package
java -jar target/helidon-example-mp-redis.jar
```
## A Glance at the Implementation
- RedisMessageConsumer: This class contains the logic for listening to the Redis channel for messages. This class uses a custom annotation '@Startup' so that it gets instantiated on application start up. The annotation '@Startup' is provided by io.helidon.examples.mp.spi.StartupInitializerExtension class in this project. To learn more about this extension, read section '16.1. Creating an Extension' in https://docs.jboss.org/weld/reference/latest/en-US/html/extend.html
- RedisMessageProducer:  This class contains  the logic for publishing messages to the Redis channel topic. It is used by GreetResource class.
- GreetResource: : This is the REST resource implementation used in this example. When updateGreeting method is called
via REST PUT operation, the new greeting sent by client is sent to Redis channel for consumption by the Redis channel subscribers
- microprofile-config.properties: This config file holds the Redis server configuration used in this example

## Exercising the Example

This is simple example that use Redis to notify interested parties when the greeting resource is getting modified.
Whenever a client modifies the greeting via PUT method call as shown below, the GreetResource::updateGreeting() implementation
publishes the modified greeting value to Redis topic. Redis consumers who listen on the specified channel, 
gets the modified greeting  messages. 

```
curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8090/greet/greeting

```
## Stop and Remove the Containers Used for this Example
```
docker stop redis-demo
```
