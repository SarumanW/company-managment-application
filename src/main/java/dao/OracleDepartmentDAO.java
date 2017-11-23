package dao;

import caching.SingletonCache;
import domain.Department;
import domain.Employee;
import connections.OracleConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleDepartmentDAO implements DepartmentDAO {
    private OracleConnection oracleConnection = new OracleConnection();
    private OracleEmployeeDAO oracleEmployeeDAO = new OracleEmployeeDAO();

    private Department extractDepartmentFromResultSet(ResultSet resultSet) throws SQLException {
        Department department = new Department();

        department.setID(resultSet.getLong(1));
        department.setName(resultSet.getString(2));

        return department;
    }

    public boolean insertDepartment(Department department) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 2)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 1003)");

            addObject.setLong(1, department.getID());
            addObject.setString(2, department.getName());
            addName.setString(1, department.getName());
            addName.setLong(2, department.getID());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();

            if(i==1 && j==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Department findDepartment(long key) {
        Department department = (Department) SingletonCache.getInstance().get(key);
        if(department!=null)
            return department;

        Connection connection = oracleConnection.getConnection();
        List<Employee> employees = new ArrayList<Employee>();

        try {
            Statement departmentStat = connection.createStatement();
            Statement employeesStat = connection.createStatement();

            ResultSet resultSet = departmentStat.executeQuery("select department.object_id as id, name.text_value as name\n" +
                    "from objects department\n" +
                    "join params name on name.OBJECT_ID = department.OBJECT_ID\n" +
                    "where department.TYPE_ID = (select type_id from types where name = 'Department')\n" +
                    "and name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'Name')\n" +
                    "and department.OBJECT_ID = " + key);

            ResultSet employeeSet = employeesStat.executeQuery("SELECT O.OBJECT_ID" +
                    "   FROM Objects O\n" +
                    "    INNER JOIN LINKS L ON L.CHILD_ID = O.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS D ON L.PARENT_ID = D.OBJECT_ID\n" +
                    "    INNER JOIN TYPES OT ON D.TYPE_ID = OT.TYPE_ID\n" +
                    "    WHERE OT.NAME = 'Department'\n" +
                    "    AND LT.NAME = 'EMPDEP'\n" +
                    "    AND D.object_id = " + key);

            while(resultSet.next()){
                department = extractDepartmentFromResultSet(resultSet);
            }

            while(employeeSet.next()){
                Employee employee = oracleEmployeeDAO.findEmployee(employeeSet.getLong(1));
                employees.add(employee);
            }

            department.setEmployees(employees);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return department;
    }

    public boolean updateDepartment(Department department) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 1003");

            updateName.setString(1, department.getName());
            updateName.setLong(2, department.getID());


            int i = updateName.executeUpdate();

            if(i==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDepartment(long key) {
        Connection connection = oracleConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate("delete from params where object_id = " + key);
            int j = statement.executeUpdate("delete from objects where object_id = " + key);
            int k = statement.executeUpdate("delete from links where parent_id = " + key);

            if(i==1 && j==1 && k==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Department> getAllDepartments() {
        Connection connection = oracleConnection.getConnection();
        List<Department> departments = new ArrayList<Department>();

        try {
            Statement departmentStat = connection.createStatement();
            Statement employeesStat = connection.createStatement();

            ResultSet resultSet = departmentStat.executeQuery("select department.object_id as id, name.text_value as name\n" +
                    "from objects department\n" +
                    "join params name on name.OBJECT_ID = department.OBJECT_ID\n" +
                    "where department.TYPE_ID = (select type_id from types where name = 'Department')\n" +
                    "and name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'Name')");

            while(resultSet.next()){
                Department department = extractDepartmentFromResultSet(resultSet);
                departments.add(department);
            }

            for(Department department : departments){
                List<Employee> employees = new ArrayList<Employee>();

                ResultSet employeeSet = employeesStat.executeQuery("SELECT O.OBJECT_ID" +
                        "   FROM Objects O\n" +
                        "    INNER JOIN LINKS L ON L.CHILD_ID = O.OBJECT_ID\n" +
                        "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                        "    INNER JOIN OBJECTS D ON L.PARENT_ID = D.OBJECT_ID\n" +
                        "    INNER JOIN TYPES OT ON D.TYPE_ID = OT.TYPE_ID\n" +
                        "    WHERE OT.NAME = 'Department'\n" +
                        "    AND LT.NAME = 'EMPDEP'\n" +
                        "    AND D.object_id = " + department.getID());

                while(employeeSet.next()){
                    Employee employee = oracleEmployeeDAO.findEmployee(employeeSet.getLong(1));
                    employees.add(employee);
                }

                department.setEmployees(employees);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }
}
