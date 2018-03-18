package domain;

import generator.UniqueID;

import java.util.*;

public class Project {
    private long projectID;
    private Calendar start;
    private Calendar end;
    private String name;
    private long managerID;
    private long customerID;
    private List<Long> sprintList;

    public Project() {
        this.setProjectID(UniqueID.generateID(this));
        this.start = Calendar.getInstance();
        this.end = new GregorianCalendar();
        this.sprintList = new ArrayList<>();
    }

    public Project(Date endDate){
        this();
        this.end = new GregorianCalendar();
        this.end.setTime(endDate);
    }

    public Project(Calendar endDate, String name){
        this();
        this.end = endDate;
        this.name = name;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public void setStart(Date date) {
        this.start = toCalendar(date);
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public void setEnd(Date date) {
        this.end = toCalendar(date);
    }

    public long getManagerID() {
        return managerID;
    }

    public void setManagerID(long managerID) {
        this.managerID = managerID;
    }

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getSprintList() {
        return sprintList;
    }

    public void setSprintList(List<Long> sprintList) {
        this.sprintList = sprintList;
    }

    private Calendar toCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", start=" + start.getTime() +
                ", end=" + end.getTime() +
                ", name='" + name + '\'' +
                ", managerID=" + managerID +
                ", customerID=" + customerID +
                ", sprintList=" + sprintList +
                '}';
    }
}
