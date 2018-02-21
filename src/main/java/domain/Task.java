package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private long taskID;
    private String name;
    private long estimate;
    private long sprintID;
    private List<Long> employees;

    public Task(){
        this.setTaskID(UniqueID.generateID(this));
        employees = new ArrayList<>();
    }

    public Task(String name, long estimate, Employee employee, Sprint sprint){
        this();
        this.name = name;
        this.estimate = estimate;
        this.employees.add(employee.getID());
        employee.getTaskList().add(this.getTaskID());
        this.sprintID = sprint.getSprintID();
        sprint.getTaskList().add(this.getTaskID());
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

    public List<Long> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Long> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee){
        this.employees.add(employee.getID());
        employee.addTask(this);
    }

    public long getSprint() {
        return sprintID;
    }

    public void setSprint(long sprint) {
        this.sprintID = sprint;
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
