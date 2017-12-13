package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    private long ID;
    private String name;
    private String surname;
    private double salary;
    private List<Project> projectList;

    public Manager(){
        this.setID(UniqueID.generateID(this));
        projectList = new ArrayList<Project>();
    }

    public Manager(String name, String surname, double salary){
        this();
        this.setName(name);
        this.setSurname(surname);
        this.setSalary(salary);
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

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", salary=" + salary +
                '}';
    }
}
