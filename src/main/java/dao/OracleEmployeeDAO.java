package dao;

import caching.SingletonCache;
import connections.OracleConnection;
import domain.Department;
import domain.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleEmployeeDAO implements EmployeeDAO {
    private OracleConnection oracleConnection = new OracleConnection();
    private OracleDepartmentDAO oracleDepartmentDAO = new OracleDepartmentDAO();

    private Employee extractEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setID(resultSet.getLong(2));

        while(resultSet.next()){
            int i = resultSet.getInt(1);
            switch (i){
                case 102:
                    employee.setName(resultSet.getString(3));
                    break;
                case 103:
                    employee.setSurname(resultSet.getString(3));
                    break;
                case 104:
                    employee.setSalary(resultSet.getDouble(4));
                    break;
            }
        }
        return employee;
    }

    public boolean insertEmployee(Employee employee) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 2)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 102)");
            PreparedStatement addSurname = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 103)");
            PreparedStatement addSalary = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (NULL, ?, ?, 104)");
            PreparedStatement addLink = connection.prepareStatement("insert into LINKS (link_id, parent_id, child_id, link_type_id) values (?, ?, ?, 150)");

            addObject.setLong(1, employee.getID());
            addObject.setString(2, employee.getSurname());

            addName.setString(1, employee.getName());
            addName.setLong(2, employee.getID());

            addSurname.setString(1, employee.getSurname());
            addSurname.setLong(2, employee.getID());

            addSalary.setDouble(1,employee.getSalary());
            addSalary.setLong(2, employee.getID());

            addLink.setLong(1, employee.getID()%13);
            addLink.setLong(2, employee.getDepartment().getID());
            addLink.setLong(3, employee.getID());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();
            int k = addSurname.executeUpdate();
            int z = addSalary.executeUpdate();
            int s = addLink.executeUpdate();

            if(i==1 && j==1 && k==1 && s==1 && z==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Employee findEmployee(long key) {
        Employee employee = (Employee) SingletonCache.getInstance().get(key);

        if(employee!=null)
            return employee;

        Connection connection = oracleConnection.getConnection();
        Department department = new Department();

        try {
            Statement statement = connection.createStatement();
            Statement departStat = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select attr.ATTRIBUTE_ID, o.object_id, p.text_value, p.NUMBER_VALUE\n" +
                    "from objects o\n" +
                    "inner join attributes attr on attr.type_id = o.TYPE_ID\n" +
                    "left join params p on p.ATTRIBUTE_ID = attr.ATTRIBUTE_ID\n" +
                    "and p.object_id = o.OBJECT_ID\n" +
                    "where o.object_id = " + key);

            ResultSet departSet = departStat.executeQuery("SELECT D.OBJECT_ID" +
                    "   FROM Objects D\n" +
                    "    INNER JOIN LINKS L ON L.PARENT_ID = D.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS E ON L.CHILD_ID = E.OBJECT_ID\n" +
                    "    INNER JOIN TYPES OT ON E.TYPE_ID = OT.TYPE_ID\n" +
                    "    WHERE OT.NAME = 'employee'\n" +
                    "    AND LT.NAME = 'dept-employee'\n" +
                    "    AND E.OBJECT_ID = " + employee.getID());

            employee = extractEmployeeFromResultSet(resultSet);

            while(departSet.next()){
                department = oracleDepartmentDAO.findDepartment(departSet.getLong(1));
            }
            employee.setDepartment(department);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        SingletonCache.getInstance().put(key, employee);
        return employee;
    }

    public boolean updateEmployee(Employee employee) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 102");
            PreparedStatement updateSurname = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 103");
            PreparedStatement updateSalary = connection.prepareStatement("update params set number_value = ? where object_id = ? and attribute_id = 104");
            PreparedStatement updateDepartment = connection.prepareStatement("update links set parent_id = ? where child_id = ? and link_type_id = 150");

            updateName.setString(1, employee.getName());
            updateName.setLong(2, employee.getID());

            updateSurname.setString(1, employee.getSurname());
            updateSurname.setLong(2, employee.getID());

            updateDepartment.setLong(1, employee.getDepartment().getID());
            updateDepartment.setLong(2, employee.getID());

            updateSalary.setDouble(1, employee.getSalary());
            updateSalary.setLong(2, employee.getID());

            int i = updateName.executeUpdate();
            int j = updateSurname.executeUpdate();
            int k = updateDepartment.executeUpdate();
            int z = updateSalary.executeUpdate();

            if(i==1 && j==1 && k==1 && z==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteEmployee(long key) {
        Connection connection = oracleConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate("delete from params where object_id = " + key);
            int j = statement.executeUpdate("delete from objects where object_id = " + key);
            int k = statement.executeUpdate("delete from links where child_id = " + key);

            if(i==1 && j==1 && k==1)
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
            Statement departStat = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select employee.object_id as id, first_name.text_value as name, last_name.text_value as surname\n" +
                    "from objects employee\n" +
                    "join params first_name on first_name.OBJECT_ID = employee.OBJECT_ID\n" +
                    "join params last_name on last_name.object_id = employee.OBJECT_ID\n" +
                    "where employee.TYPE_ID = (select type_id from types where name = 'employee')\n" +
                    "and first_name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'emp_name')\n" +
                    "and last_name.ATTRIBUTE_ID in (select attribute_id from attributes where name = 'emp_surname')");

            while(resultSet.next()){
                Employee employee = extractEmployeeFromResultSet(resultSet);
                employees.add(employee);
            }

            for(Employee employee: employees){
                Department department = null;

                ResultSet depart = departStat.executeQuery("SELECT D.OBJECT_ID, D.name\n" +
                        "   FROM Objects D\n" +
                        "    INNER JOIN LINKS L ON L.PARENT_ID = D.OBJECT_ID\n" +
                        "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                        "    INNER JOIN OBJECTS E ON L.CHILD_ID = E.OBJECT_ID\n" +
                        "    INNER JOIN TYPES OT ON E.TYPE_ID = OT.TYPE_ID\n" +
                        "    WHERE OT.NAME = 'employee'\n" +
                        "    AND LT.NAME = 'dept_employee'\n" +
                        "    AND E.OBJECT_ID = " + employee.getID());

                while(depart.next()){
                    department = oracleDepartmentDAO.findDepartment(depart.getLong(1));
                }

                employee.setDepartment(department);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }
}
