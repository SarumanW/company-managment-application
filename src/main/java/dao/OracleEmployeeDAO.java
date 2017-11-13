package dao;

import connections.OracleConnection;
import domain.Department;
import domain.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OracleEmployeeDAO implements EmployeeDAO {
    private OracleConnection oracleConnection = new OracleConnection();

    private Employee extractEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();

        employee.setID(resultSet.getLong("ID"));
        employee.setName(resultSet.getString("NAME"));
        employee.setName(resultSet.getString("SURNAME"));
        employee.setDepartment((Department) resultSet.getObject("DEPARTMENT"));

        return employee;
    }

    public int insertEmployee() {
        return 0;
    }

    public Employee findEmployee(int key) {
        Connection connection = oracleConnection.getConnection();

        return null;
    }

    public boolean updateEmployee(Employee employee) {
        return false;
    }

    public boolean deleteEmployee(Employee employee) {
        return false;
    }

    public List<Employee> getAllEmployees() {
        return null;
    }
}
