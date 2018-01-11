package dao;

import dao.dao_interface.*;

import java.sql.Connection;

public abstract class DAOFactory {
    public abstract EmployeeDAO getEmployeeDAO();
    public abstract DepartmentDAO getDepartmentDAO();
    public abstract ManagerDAO getManagerDAO();
    public abstract CustomerDAO getCustomerDAO();
    public abstract ProjectDAO getProjectDAO();
    public abstract SprintDAO getSprintDAO();
    public abstract TaskDAO getTaskDAO();
}
