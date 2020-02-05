package com.jobinesh.example.grpc.hr.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobinesh.example.grpc.hr.service.Department;
import com.jobinesh.example.grpc.hr.service.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentEntity {
    private Long id;
    private String departmentName;
    private String location;
    @JsonProperty("employees")
    private List<EmployeeEntity> employeeEntities;

    public DepartmentEntity() {
    }

    public DepartmentEntity(Long id, String departmentName, String location, List<EmployeeEntity> employees) {
        this.id = id;
        this.departmentName = departmentName;
        this.location = location;
        this.employeeEntities = employees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<EmployeeEntity> getEmployeeEntities() {
        return employeeEntities;
    }

    public void setEmployeeEntities(List<EmployeeEntity> employeeEntities) {
        this.employeeEntities = employeeEntities;
    }

    public Department toProto(){
        List<Employee> employees = employeeEntities.stream().map(EmployeeEntity::toProto).collect(Collectors.toList());
        return  Department.newBuilder().setId(getId())
                .setDepartmentName(getDepartmentName())
                .setLocation(getLocation())
                .addAllEmployees(employees)
                .build();
    }

    public static DepartmentEntity fromProto(Department deptRequest){
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setDepartmentName(deptRequest.getDepartmentName());
        departmentEntity.setId(deptRequest.getId());
        departmentEntity.setLocation(deptRequest.getLocation());
        List<EmployeeEntity> employeeEntities = deptRequest.getEmployeesList().stream().map(EmployeeEntity::fromProto).collect(Collectors.toList());
        departmentEntity.setEmployeeEntities( employeeEntities);
        return departmentEntity;
    }
}
