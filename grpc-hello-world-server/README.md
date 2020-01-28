This is a very basic example showcasing how to build a simple gRPC server and a client. Here are the steps:

## Create a gRPC Service
```
mvn archetype:generate -DgroupId=com.jobinesh.example.grpc  -DartifactId=grpc-hello-world-server  -DarchetypeArtifactId=maven-archetype-quickstart  -DinteractiveMode=false
```
## Add a gRPC Definition File
```
$ cd grpc-hello-world-server  
$ mkdir -p src/main/proto
```
Create a new proto file src/main/proto/HelloWorldService.proto
Copy the contents from this file to the newly craeted proto file: https://github.com/jobinesh/cloud-native-applications/blob/master/grpc-hello-world-server/src/main/proto/HelloWorldService.proto

## Add the dependencies

Add the gRPC dependencies and Plugin as shown in the following pom.xml: https://github.com/jobinesh/cloud-native-applications/blob/master/grpc-hello-world-server/pom.xml

## Generate the Stubs
```
$ mvn -DskipTests package
```
## Implement the HelloWorld Service

Refer to this file to know how a typical implementation code may look like: https://github.com/jobinesh/cloud-native-applications/blob/master/grpc-hello-world-server/src/main/java/com/jobinesh/example/grpc/server/HelloWorldServiceImpl.java

## Implement the Server

Edit the App class and add the implementation. You can refer to the following file to know the APIs and thier usage pattern: https://github.com/jobinesh/cloud-native-applications/blob/master/grpc-hello-world-server/src/main/java/com/jobinesh/example/grpc/server/App.java

## Run the Server
```
$ mvn -DskipTests package exec:java -Dexec.mainClass=com.jobineh.example.grpc.server.App
```
Server console output the following

```
Server started
```

## Add the Client

Add a new client for exercising the above service. Here is an example: https://github.com/jobinesh/cloud-native-applications/blob/master/grpc-hello-world-server/src/main/java/com/jobinesh/example/grpc/client/Client.java

## Consume the gRPC APIs with the Client
```
$ mvn -DskipTests package exec:java -Dexec.mainClass=com.example.grpc.cleint.Client
```
You may notice the following output on the client console:
```
greeting: "Hello there, Jobinesh"
```
The official quickstart tutorial is available here: https://codelabs.developers.google.com/codelabs/cloud-grpc-java/index.html
