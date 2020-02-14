package com.jobinesh.example.grpc.helidon.hr.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.jobinesh.example.grpc.helidon.hr.data.DepartmentEntity;
import com.jobinesh.example.grpc.helidon.hr.data.HRServiceDataRepository;
import com.jobinesh.example.grpc.hr.service.Department;
import com.jobinesh.example.grpc.hr.service.DepartmentFilter;
import com.jobinesh.example.grpc.hr.service.DepartmentList;
import com.jobinesh.example.grpc.hr.service.HRServiceGrpc;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.stream.Collectors;

public class HRServiceGrpcImpl extends HRServiceGrpc.HRServiceImplBase {


    HRServiceDataRepository hrServiceDataRepository = new HRServiceDataRepository();

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void createDepartment(Department request, StreamObserver<Department> responseObserver) {
        DepartmentEntity entry = DepartmentEntity.fromProto(request);
        entry = hrServiceDataRepository.createDepartment(entry);
        responseObserver.onNext(entry.toProto());
        responseObserver.onCompleted();
    }

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void updateDepartment(Department request, StreamObserver<Department> responseObserver) {
        DepartmentEntity entry = DepartmentEntity.fromProto(request);
        entry = hrServiceDataRepository.updateDepartment(entry);
        responseObserver.onNext(entry.toProto());
        responseObserver.onCompleted();
    }

    /**
     * @param filter
     * @param responseObserver
     */
    @Override
    public void findDepartmentsByFilter(DepartmentFilter filter, StreamObserver<DepartmentList> responseObserver) {
        List<DepartmentEntity> deptEntities = hrServiceDataRepository.findDepartmentsByFilter(filter);
        List<Department> depts = deptEntities.stream().map(DepartmentEntity::toProto).collect(Collectors.toList());
        DepartmentList departmentList = DepartmentList.newBuilder().addAllResultList(depts)
                .setResultCount(Int64Value.newBuilder().setValue(deptEntities.size()).build()).build();
        responseObserver.onNext(departmentList);
        responseObserver.onCompleted();
    }

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void deleteDepartment(Int64Value request, StreamObserver<Empty> responseObserver) {
        hrServiceDataRepository.deleteDepartment(request.getValue());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    /**
     * @param id
     * @param responseObserver
     */
    @Override
    public void findDepartmentById(Int64Value id, StreamObserver<Department> responseObserver) {
        DepartmentEntity departmentEntity = hrServiceDataRepository.findDepartmentById(id.getValue());
        if (departmentEntity == null) {
            Metadata metadata = new Metadata();
            responseObserver.onError(
                    Status.UNAVAILABLE.withDescription("Department not found !")
                            .asRuntimeException(metadata));
        } else {
            responseObserver.onNext(departmentEntity.toProto());
            responseObserver.onCompleted();
        }

    }

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void findAllDepartments(Empty request, StreamObserver<Department> responseObserver) {
        List<DepartmentEntity> deptEntities = hrServiceDataRepository.findAllDepartments();
        for (DepartmentEntity dept : deptEntities) {
            responseObserver.onNext(dept.toProto());
        }
        responseObserver.onCompleted();
    }

    /**
     * @param responseObserver
     */
    @Override
    public StreamObserver<Department> updateDepartmentsInBatch(StreamObserver<Department> responseObserver) {
        return new StreamObserver<Department>() {
            @Override
            public void onNext(Department request) {

                DepartmentEntity savedDept = hrServiceDataRepository.updateDepartment( DepartmentEntity.fromProto(request));
                responseObserver.onNext(savedDept.toProto());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
