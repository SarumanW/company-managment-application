package domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Project {
    private long projectID;
    private Calendar start;
    private Calendar end;
    private String name;
    private SimpleDateFormat format;
    private Manager manager;
    private Customer customer;

    public Project(){
        start = new GregorianCalendar();
        format = new SimpleDateFormat("dd-MM-yyyy");
    }

    public Project(long projectID, String endDate){
        this();
        end = new GregorianCalendar();
        try {
            end.setTime(format.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
