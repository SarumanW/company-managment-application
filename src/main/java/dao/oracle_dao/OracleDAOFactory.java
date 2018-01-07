package dao.oracle_dao;

import dao.DAOFactory;
import dao.dao_interface.DepartmentDAO;
import dao.dao_interface.EmployeeDAO;
import dao.dao_interface.ManagerDAO;

import java.sql.Connection;

public class OracleDAOFactory extends DAOFactory {

    public EmployeeDAO getEmployeeDAO(Connection connection) {
        return new OracleEmployeeDAO();
    }

    public DepartmentDAO getDepartmentDAO(Connection connection) {
        return new OracleDepartmentDAO();
    }

    public ManagerDAO getManagerDAO(Connection connection){
        return  new OracleManagerDAO();
    }
}
