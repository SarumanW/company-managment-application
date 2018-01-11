package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private long ID;
    private String name;
    private String surname;
    private double salary;
    private long departmentID;
    private List<Long> taskList;

    public Employee(){
        this.setID(UniqueID.generateID(this));
        taskList = new ArrayList<>();
    }

    public Employee(String name, String surname, double salary, long departmentID){
        this();
        this.setName(name);
        this.setSurname(surname);
        this.setSalary(salary);
        this.setDepartment(departmentID);
        //department.getEmployees().add(this);
    }
    public long getDepartment() {
        return departmentID;
    }

    public void setDepartment(long departmentID) {
        this.departmentID = departmentID;
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

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<Long> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Long> taskList) {
        this.taskList = taskList;
    }

    public void addTask(Task task){
        //this.taskList.add(task);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", salary=" + salary +
                ", departmentID=" + departmentID +
                ", taskList=" + taskList +
                '}';
    }
}
