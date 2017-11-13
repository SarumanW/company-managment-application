package dao;

import java.sql.Connection;

public class OracleDAOFactory extends DAOFactory {

    public EmployeeDAO getEmployeeDAO(Connection connection) {
        return new OracleEmployeeDAO();
    }

    public DepartmentDAO getDepartmentDAO(Connection connection) {
        return new OracleDepartmentDAO();
    }
}
