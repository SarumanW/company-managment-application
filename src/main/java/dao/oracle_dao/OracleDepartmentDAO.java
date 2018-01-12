package dao.oracle_dao;

import caching.SingletonCache;
import dao.dao_interface.DepartmentDAO;
import domain.Department;
import domain.Employee;
import connections.OracleConnection;
import generator.UniqueID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleDepartmentDAO implements DepartmentDAO {
    private OracleConnection oracleConnection;

    public OracleDepartmentDAO(){
        oracleConnection = new OracleConnection();
    }

    private Department extractDepartmentFromResultSet(ResultSet resultSet) throws SQLException {
        Department department = new Department();

        while(resultSet.next()){
            int i = resultSet.getInt(1);
            switch (i){
                case 101:
                    department.setName(resultSet.getString(3));
                    break;
            }
        }
        return department;
    }

    @Override
    public boolean insertDepartment(Department department) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 1)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 101)");
            PreparedStatement addLink = connection.prepareStatement("insert into LINKS (link_id, parent_id, child_id, link_type_id) values (?, ?, ?, 150)");

            addObject.setLong(1, department.getID());
            addObject.setString(2, department.getName());

            addName.setString(1, department.getName());
            addName.setLong(2, department.getID());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();

            if(department.getEmployees().size() != 0){
                for(long employee : department.getEmployees()){
                    addLink.setLong(1, UniqueID.generateID(new Object()));
                    addLink.setLong(2, department.getID());
                    addLink.setLong(3, employee);
                    addLink.executeUpdate();
                }
            }

            if(i==1 && j==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Department findDepartment(long key) {
        Department department = (Department) SingletonCache.getInstance().get(key);
        if(department!=null)
            return department;

        Connection connection = oracleConnection.getConnection();
        List<Long> employees = new ArrayList<>();

        try {
            Statement departmentStat = connection.createStatement();
            Statement employeesStat = connection.createStatement();

            ResultSet resultSet = departmentStat.executeQuery("select attr.ATTRIBUTE_ID, o.object_id, p.text_value\n" +
                    "from objects o\n" +
                    "inner join attributes attr on attr.type_id = o.TYPE_ID\n" +
                    "left join params p on p.ATTRIBUTE_ID = attr.ATTRIBUTE_ID\n" +
                    "and p.object_id = o.OBJECT_ID\n" +
                    "where o.object_id = " + key);

            ResultSet employeeSet = employeesStat.executeQuery("SELECT O.OBJECT_ID" +
                    "   FROM Objects O\n" +
                    "    INNER JOIN LINKS L ON L.CHILD_ID = O.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS D ON L.PARENT_ID = D.OBJECT_ID\n" +
                    "    INNER JOIN TYPES OT ON D.TYPE_ID = OT.TYPE_ID\n" +
                    "    WHERE OT.TYPE_ID = 1\n" +
                    "    AND LT.LINK_TYPE_ID = 150\n" +
                    "    AND D.object_id = " + key);

            department = extractDepartmentFromResultSet(resultSet);
            department.setID(key);

            while(employeeSet.next()){
                long employee = employeeSet.getLong(1);
                employees.add(employee);
            }
            department.setEmployees(employees);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        SingletonCache.getInstance().put(key, department);
        return department;
    }

    @Override
    public boolean updateDepartment(Department department) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 101");
            PreparedStatement updateEmployee = connection.prepareStatement("update links set parent_id = ? where child_id = ? and link_type_id = 150");

            updateName.setString(1, department.getName());
            updateName.setLong(2, department.getID());

            int i = updateName.executeUpdate();

            for(Long employeeID : department.getEmployees()){
                updateEmployee.setLong(1, department.getID());
                updateEmployee.setLong(2, employeeID);
                updateEmployee.executeUpdate();
            }

            if(i==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
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
}
