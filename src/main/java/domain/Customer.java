package domain;

import generator.UniqueID;

public class Customer {
    private long customerID;
    private String name;
    private String surname;
    private long projectID;

    public Customer(){
        this.setCustomerID(UniqueID.generateID(this));
    }

    public Customer (String name, String surname){
        this();
        this.name = name;
        this.surname = surname;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProject(long projectID) {
        this.projectID = projectID;
    }
}
