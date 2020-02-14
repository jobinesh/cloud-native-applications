
## A Simple Department-Employee Example Illustrating Create Read Update Delete (CRUD) operations with gRPC APIs 
To Run this example do the following
1. Clone the repository https://github.com/jobinesh/cloud-native-applications.git 
2. Open a terminal and perfrom the following
```
$ cd  cloud-native-applications/grpc-helidon-hr-example
$ mvn install
```    

3. Start the Helidon SE based gRPC Server
```
 $ cd  grpc-helidon-hr-service 
 $ mvn exec:java 
```  
4. Run the Client
```
 $ cd  grpc-helidon-hr-client  
 $  mvn exec:java -DskipTests -Dexec.mainClass=com.jobinesh.example.grpc.helidon.hr.client.GrpcClient
```
