package domain;

import generator.UniqueID;

public class Employee {
    private long ID;
    private String name;
    private String surname;
    private double salary;
    private Department department;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee(){
        this.setID(UniqueID.generateID(this));
    }

    public Employee(String name, String surname, double salary){
        this();
        this.setName(name);
        this.setSurname(surname);
        this.setSalary(salary);
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

    @Override
    public String toString() {
        String s = ID + " " + name + " " + surname + " " + department.getName();
        return s;
    }
}
