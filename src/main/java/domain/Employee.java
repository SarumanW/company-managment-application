package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private long employeeID;
    private String name;
    private String surname;
    private double salary;
    private long departmentID;
    private List<Long> taskList;

    public Employee(){
        this.setEmployeeID(UniqueID.generateID(this));
        taskList = new ArrayList<>();
    }

    public Employee(String name, String surname, double salary, Department department){
        this();
        this.setName(name);
        this.setSurname(surname);
        this.setSalary(salary);
        this.setDepartmentID(department.getDepartmentID());
        department.getEmployees().add(this.getEmployeeID());
    }

    public long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(long departmentID) {
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

    public long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(long ID) {
        this.employeeID = ID;
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
        this.taskList.add(task.getTaskID());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "ID=" + employeeID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", salary=" + salary +
                ", departmentID=" + departmentID +
                ", taskList=" + taskList +
                '}';
    }
}
