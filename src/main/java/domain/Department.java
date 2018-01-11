package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private long ID;
    private String name;
    private List<Long> employees;

    public Department(){
        this.setID(UniqueID.generateID(this));
        employees = new ArrayList<>();
    }

    public Department(String name){
        this();
        this.name = name;
    }

    public List<Long> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Long> employees) {
        this.employees = employees;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", employees=" + employees +
                '}';
    }
}
