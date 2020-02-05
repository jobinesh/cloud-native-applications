package com.jobinesh.example.grpc.hr.data;

import com.jobinesh.example.grpc.hr.service.Employee;

public class EmployeeEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    public EmployeeEntity() {
    }

    public EmployeeEntity(Long id, String firstName, String lastName, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Employee toProto(){

        return  Employee.newBuilder().setId(getId())
                .setEmail(getEmail())
                .setFirstName(getFirstName())
                .setLastName(getLastName())
                .setPhone(getPhone()).build();
    }

    public static EmployeeEntity fromProto(Employee empRequest){
        EmployeeEntity  empEntity = new EmployeeEntity();
        empEntity.setId(empRequest.getId());
        empEntity.setEmail(empRequest.getEmail());
        empEntity.setFirstName(empRequest.getFirstName());
        empEntity.setLastName(empRequest.getLastName());
        empEntity.setPhone(empRequest.getPhone());
        return empEntity;
    }
}
