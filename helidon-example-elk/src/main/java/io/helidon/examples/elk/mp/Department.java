package io.helidon.examples.elk.mp;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String id;
    private String name;
    private String location;
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee emp){
        employees.add(emp);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
