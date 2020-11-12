package io.helidon.examples.elk.mp;

public class HRDoc {
    private String id;
    private String employeeId;
    private String departmentName;
    private String location;
    private String firstName;
    private String lastName;
    private String sex;
    private RelationType relationType;

    public HRDoc() {
    }

    public HRDoc(String id, String name, String location) {
        this.id = id;
        this.departmentName = name;
        this.location = location;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "HRDoc{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", location='" + location + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex='" + sex + '\'' +
                ", relationType=" + relationType +
                '}';
    }

    public static class RelationType{
        private String name;
        private String parent;

        public RelationType() {
        }

        public RelationType(String name, String parent) {
            this.name = name;
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "RelationType{" +
                    "name='" + name + '\'' +
                    ", parent='" + parent + '\'' +
                    '}';
        }
    }
}
