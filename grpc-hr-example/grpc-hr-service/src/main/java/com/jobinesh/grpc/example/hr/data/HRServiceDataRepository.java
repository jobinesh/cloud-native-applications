package com.jobinesh.grpc.example.hr.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobinesh.grpc.example.hr.service.DepartmentFilter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HRServiceDataRepository {
    Map<Long, DepartmentEntity> departmentsMap;

    public HRServiceDataRepository() {
        departmentsMap = readHRDataFromFileStore();
    }

    public DepartmentEntity createDepartment(DepartmentEntity department) {
        departmentsMap.put(department.getId(), department);
        return department;
    }


    public DepartmentEntity updateDepartment(DepartmentEntity department) {
        departmentsMap.put(department.getId(), department);
        return department;
    }

    public List<DepartmentEntity> findDepartmentsByFilter(DepartmentFilter departmentFilter) {
        List<DepartmentEntity> filteredList = departmentsMap.values().stream()
                .filter(x -> x.getId().equals(departmentFilter.getId())
                        || x.getLocation().equals(departmentFilter.getLocation())
                        || x.getDepartmentName().equals( departmentFilter.getDepartmentName())
                )
                .collect(Collectors.toList());
        return filteredList;
    }


    public void deleteDepartment(Long departmentId) {
        departmentsMap.remove(departmentId);
    }

    public DepartmentEntity findDepartmentById(Long id) {
        if(id<0){
           throw new  IllegalArgumentException("Department Id cannot be negative!");
        }
        return departmentsMap.get(id);
    }

    private Map<Long, DepartmentEntity> readHRDataFromFileStore() {
        List<DepartmentEntity> deptsList = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream jsonInput = HRServiceDataRepository.class.getResourceAsStream("/hr-data.json");
            deptsList = mapper.readValue(jsonInput,
                    new TypeReference<List<DepartmentEntity>>() {
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
            deptsList = new ArrayList<DepartmentEntity>();
        }
        Map<Long, DepartmentEntity> deptsMap = deptsList.stream().
                collect(Collectors.toMap(DepartmentEntity::getId, dept -> dept));
        return deptsMap;
    }
}
