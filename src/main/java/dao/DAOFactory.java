package dao;

import dao.dao_interface.DepartmentDAO;
import dao.dao_interface.EmployeeDAO;

import java.sql.Connection;

public abstract class DAOFactory {
//    public Connection getConnection(){
//        OracleConnection oracleConnection = new OracleConnection();
//        return oracleConnection.getConnection();
//    }
    public abstract EmployeeDAO getEmployeeDAO(Connection connection);
    public abstract DepartmentDAO getDepartmentDAO(Connection connection);
}
