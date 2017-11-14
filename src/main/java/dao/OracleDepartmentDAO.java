package dao;

import domain.Department;
import domain.Employee;
import connections.OracleConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class OracleDepartmentDAO implements DepartmentDAO {
    private OracleConnection oracleConnection = new OracleConnection();

    public int insertDepartment() {
        return 0;
    }

    public Department findDepartment(int key) {
        Connection connection = oracleConnection.getConnection();
        Department department = new Department();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("" + key);

            if(resultSet.next()){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return department;
    }

    public boolean updateDepartment(Department department) {
        return false;
    }

    public boolean deleteDepartment(Department department) {
        return false;
    }

    public List<Department> getAllDepartments() {
        return null;
    }
}
