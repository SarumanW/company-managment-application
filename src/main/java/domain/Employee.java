package domain;

import generator.UniqueID;

public class Employee {
    private long ID;
    private String name;
    private String surname;
//    private Department department;

//    public Department getDepartment() {
//        return department;
//    }

//    public void setDepartment(Department department) {
//        this.department = department;
//    }

    public Employee(){
        this.setID(UniqueID.generateID(this));
    }

    public Employee(String name, String surname){
        this();
        this.setName(name);
        this.setSurname(surname);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = this.surname;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
