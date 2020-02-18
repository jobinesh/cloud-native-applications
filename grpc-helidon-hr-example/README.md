
## A Simple Department-Employee Example Illustrating Create Read Update Delete (CRUD) operations with gRPC APIs 
To Run this example do the following
1. Clone the repository https://github.com/jobinesh/cloud-native-applications.git 
2. Open a terminal and perfrom the following
```
$ cd  cloud-native-applications/grpc-helidon-hr-example
$ mvn install
```    

3. Start the Helidon SE based gRPC Server. The below mvn command runs the com.jobinesh.example.grpc.helidon.hr.service.HelidonSEServer class
```
 $ cd  grpc-helidon-hr-service 
 $ mvn exec:java 
```  
4. The following APIs are exposed by the server used in this example
```
  rpc createDepartment (Department) returns (Department) {};
  rpc updateDepartment (Department) returns (Department) {};
  rpc findDepartmentsByFilter(DepartmentFilter) returns (DepartmentList) {};
  rpc deleteDepartment(google.protobuf.Int64Value) returns (google.protobuf.Empty) {};
  rpc findDepartmentById (google.protobuf.Int64Value) returns (Department) {};
  rpc findAllDepartments(google.protobuf.Empty) returns (stream Department) {};
  rpc updateDepartmentsInBatch(stream Department) returns (stream Department){};
```
5. Run the Client
```
 $ cd  grpc-helidon-hr-client  
 $  mvn exec:java -DskipTests -Dexec.mainClass=com.jobinesh.example.grpc.helidon.hr.client.GrpcClient
```
6. We are running our gRPC API as Heldion SE microservice in this example. The advantage here is that we can leverage the  built-in Helidon SE offerings for monitoring and tracing the gRPC APIs. For instance the following URL gives you health check info for our example: http://localhost:8080/health
