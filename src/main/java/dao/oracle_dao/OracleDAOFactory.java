package dao.oracle_dao;

import dao.DAOFactory;
import dao.dao_interface.*;

import java.sql.Connection;

public class OracleDAOFactory extends DAOFactory {

    @Override
    public EmployeeDAO getEmployeeDAO() {
        return new OracleEmployeeDAO();
    }

    @Override
    public DepartmentDAO getDepartmentDAO() {
        return new OracleDepartmentDAO();
    }

    @Override
    public ManagerDAO getManagerDAO(){
        return  new OracleManagerDAO();
    }

    @Override
    public CustomerDAO getCustomerDAO() {
        return new OracleCustomerDAO();
    }

    @Override
    public ProjectDAO getProjectDAO() {
        return new OracleProjectDAO();
    }

    @Override
    public SprintDAO getSprintDAO() {
        return new OracleSprintDAO();
    }

    @Override
    public TaskDAO getTaskDAO() {
        return new OracleTaskDAO();
    }
}
