package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private long ID;
    private String name;
    private String surname;
    private double salary;
    private Department department;
    private List<Task> taskList;

    public Employee(){
        this.setID(UniqueID.generateID(this));
        taskList = new ArrayList<>();
        department = new Department();
    }

    public Employee(String name, String surname, double salary, Department department){
        this();
        this.setName(name);
        this.setSurname(surname);
        this.setSalary(salary);
        this.setDepartment(department);
        department.getEmployees().add(this);
    }
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        String s = ID + " " + name + " " + surname + " " + department.getName();
        return s;
    }

    public void addTask(Task task){
        this.taskList.add(task);
    }
}
