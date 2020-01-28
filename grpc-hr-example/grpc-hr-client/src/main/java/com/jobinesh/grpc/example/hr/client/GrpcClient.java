package com.jobinesh.grpc.example.hr.client;

import com.google.protobuf.Int64Value;
import com.jobinesh.grpc.example.hr.service.Department;
import com.jobinesh.grpc.example.hr.service.DepartmentFilter;
import com.jobinesh.grpc.example.hr.service.DepartmentList;
import com.jobinesh.grpc.example.hr.service.HRServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

/**
 * Grpc Client
 */
public class GrpcClient {
    private ManagedChannel channel;
    private HRServiceGrpc.HRServiceBlockingStub hrServiceStub;

    public GrpcClient() {
        initializeStub();
    }

    public static void main(String[] args) {

        System.out.println("gRPC Client");
        GrpcClient client = new GrpcClient();
        client.updateDepartment(1000L);
        Department dept = client.findDepartmentById(1000L);
        log("Department::" + dept);
        DepartmentFilter filter = DepartmentFilter.newBuilder().setDepartmentName("IT").build();
        List<Department> depts = client.findDepartmentByFilter(filter);
        log("Departments::" + depts);
        client.deleteDepartment(1000L);
        dept = client.findDepartmentById(1000L);
        log("Department::" + dept);
        client.shutDown();

    }

    private static void log(String message) {
        System.out.println(message);
    }

    private void initializeStub() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext(true).build();
        hrServiceStub = HRServiceGrpc.newBlockingStub(channel);
    }

    public void shutDown() {
        channel.shutdownNow();
    }

    private Department findDepartmentById(Long id) {
        return hrServiceStub.findDepartmentById(Int64Value.of(id));
    }

    public  Department updateDepartment(Long id) {
        Department dept = Department.newBuilder().setId(id)
                .setDepartmentName("Administration")
                .setLocation("Foster City")
                .build();
        hrServiceStub.updateDepartment(dept);
        return dept;
    }

    public List<Department> findDepartmentByFilter(DepartmentFilter filter) {

        DepartmentList departmentList = hrServiceStub.findDepartmentsByFilter(filter);
        return departmentList.getResultListList();
    }

    public void deleteDepartment(Long id){
        hrServiceStub.deleteDepartment(Int64Value.of(id));
    }
}
