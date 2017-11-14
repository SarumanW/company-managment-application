package dao;

import connections.OracleConnection;
import domain.Department;
import domain.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleEmployeeDAO implements EmployeeDAO {
    private OracleConnection oracleConnection = new OracleConnection();

    private Employee extractEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();

        employee.setID(resultSet.getLong("id"));
        employee.setName(resultSet.getString("name"));
        employee.setName(resultSet.getString("surname"));

        return employee;
    }

    public boolean insertEmployee(Employee employee) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 1)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 1001)");
            PreparedStatement addSurname = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 1002)");

            addObject.setLong(1, employee.getID());
            addObject.setString(2, employee.getSurname());
            addName.setString(1, employee.getName());
            addName.setLong(2, employee.getID());
            addSurname.setString(1, employee.getSurname());
            addSurname.setLong(2, employee.getID());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();
            int k = addSurname.executeUpdate();

            if(i==1 && j==1 && k==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Employee findEmployee(int key) {
        Connection connection = oracleConnection.getConnection();
        Employee employee = new Employee();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select employee.object_id as id, first_name.text_value as name, last_name.text_value as surname\n" +
                    "from objects employee\n" +
                    "join params first_name on first_name.OBJECT_ID = employee.OBJECT_ID\n" +
                    "join params last_name on last_name.object_id = employee.OBJECT_ID\n" +
                    "where employee.TYPE_ID = (select type_id from types where name = 'Employee')\n" +
                    "and first_name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'Name')\n" +
                    "and last_name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'Surname')\n" +
                    "and employee.OBJECT_ID = " + key);

            if(resultSet.next()){
                return extractEmployeeFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }

    public boolean updateEmployee(Employee employee) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 1001");
            PreparedStatement updateSurname = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 1002");

            updateName.setString(1, employee.getName());
            updateName.setLong(2, employee.getID());
            updateSurname.setString(1, employee.getSurname());
            updateSurname.setLong(2, employee.getID());

            int i = updateName.executeUpdate();
            int j = updateSurname.executeUpdate();

            if(i==1 && j==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(int key) {
        Connection connection = oracleConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate("delete from params where object_id = " + key);
            int j = statement.executeUpdate("delete from objects where object_id = " + key);

            if(i==1 && j==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Employee> getAllEmployees() {
        Connection connection = oracleConnection.getConnection();
        List<Employee> employees = new ArrayList<Employee>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select employee.object_id as id, first_name.text_value as name, last_name.text_value as surname\n" +
                    "from objects employee\n" +
                    "join params first_name on first_name.OBJECT_ID = employee.OBJECT_ID\n" +
                    "join params last_name on last_name.object_id = employee.OBJECT_ID\n" +
                    "where employee.TYPE_ID = (select type_id from types where name = 'Employee')\n" +
                    "and first_name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'Name')\n" +
                    "and last_name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'Surname')");

            if(resultSet.next()){
                Employee employee = extractEmployeeFromResultSet(resultSet);
                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }
}
