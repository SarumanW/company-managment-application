package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private long managerID;
    private String name;
    private String surname;
    private double salary;
    private List<Long> projectList;

    public Manager(){
        this.setManagerID(UniqueID.generateID(this));
        projectList = new ArrayList<>();
    }

    public Manager(String name, String surname, double salary){
        this();
        this.setName(name);
        this.setSurname(surname);
        this.setSalary(salary);
    }

    public long getManagerID() {
        return managerID;
    }

    public void setManagerID(long ID) {
        this.managerID = ID;
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
        this.surname = surname;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<Long> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Long> projectList) {
        this.projectList = projectList;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "managerID=" + managerID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", salary=" + salary +
                ", projectList=" + projectList +
                '}';
    }
}
