
## A Simple Department-Employee Example Illustrating Create Read Update Delete (CRUD) operations with gRPC APIs 
To Run this example do the following
1. Clone the repository https://github.com/jobinesh/cloud-native-applications.git 
2. Open a terminal and perfrom the following
```
$ cd  cloud-native-applications/grpc-hr-example
$ mvn install
```    

3. Start the gRPC Server
```
 $ cd  grpc-hr-service 
 $ mvn exec:java  -Dexec.mainClass="com.jobinesh.grpc.example.hr.service.GrpcServer"
```  
4. Run the Client
```
 $ cd  grpc-hr-client  
 $ mvn exec:java  -Dexec.mainClass="com.jobinesh.grpc.example.hr.client.GrpcClient"
```
