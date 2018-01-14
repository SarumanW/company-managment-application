package domain;

import generator.UniqueID;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        start = Calendar.getInstance();
        end = new GregorianCalendar();
        sprintList = new ArrayList<>();
    }

    public Project(Date endDate){
        this();
        end = new GregorianCalendar();
        end.setTime(endDate);
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

    public List<Long> getSprints() {
        return sprintList;
    }

    public void setSprints(List<Long> sprintList) {
        this.sprintList = sprintList;
    }

    private Calendar toCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
