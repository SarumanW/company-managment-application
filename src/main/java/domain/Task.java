package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private long taskID;
    private String name;
    private long estimate;
    private long sprintID;
    private List<Long> employeeList;

    public Task(){
        this.setTaskID(UniqueID.generateID(this));
        employeeList = new ArrayList<>();
    }

    public Task(String name, long estimate, Employee employee, Sprint sprint){
        this();
        this.name = name;
        this.estimate = estimate;
        this.employeeList.add(employee.getEmployeeID());
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

    public List<Long> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Long> employeeList) {
        this.employeeList = employeeList;
    }

    public void addEmployee(Employee employee){
        this.employeeList.add(employee.getEmployeeID());
        employee.addTask(this);
    }

    public long getSprintID() {
        return sprintID;
    }

    public void setSprintID(long sprint) {
        this.sprintID = sprint;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", name='" + name + '\'' +
                ", estimate=" + estimate +
                ", employeeList=" + employeeList +
                '}';
    }
}
