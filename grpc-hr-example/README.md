
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
 $ mvn exec:java  -Dexec.mainClass="com.jobinesh.example.grpc.hr.service.GrpcServer"
```  
4. The following APIs are exposed by this server
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
 $ cd  grpc-hr-client  
 $ mvn exec:java  -Dexec.mainClass="com.jobinesh.example.grpc.hr.client.GrpcClient"
```
