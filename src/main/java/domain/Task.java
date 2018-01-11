package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private long taskID;
    private String name;
    private long estimate;
    private List<Employee> employees;

    public Task(){
        this.setTaskID(UniqueID.generateID(this));
        employees = new ArrayList<>();
    }

    public Task(String name, long estimate){
        this();
        this.name = name;
        this.estimate = estimate;
    }

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEstimate() {
        return estimate;
    }

    public void setEstimate(long estimate) {
        this.estimate = estimate;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee){
        this.employees.add(employee);
        employee.addTask(this);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", name='" + name + '\'' +
                ", estimate=" + estimate +
                ", employees=" + employees +
                '}';
    }
}
