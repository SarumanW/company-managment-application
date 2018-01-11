package dao.oracle_dao;

import caching.SingletonCache;
import connections.OracleConnection;
import dao.dao_interface.TaskDAO;
import domain.Employee;
import domain.Sprint;
import domain.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleTaskDAO implements TaskDAO {
    private OracleConnection oracleConnection = new OracleConnection();
    private OracleEmployeeDAO oracleEmployeeDAO = new OracleEmployeeDAO();

    private Task extractTaskFromResultSet(ResultSet resultSet) throws SQLException {
        Task task = new Task();
        task.setTaskID(resultSet.getLong(2));

        while(resultSet.next()){
            int i = resultSet.getInt(1);
            switch(i){
                case 110:
                    task.setName(resultSet.getString(3));
                    break;
                case 111:
                    task.setEstimate(resultSet.getLong(4));
                    break;
            }
        }

        return task;
    }
    @Override
    public boolean insertTask(Task task) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 7)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, object_id, attribute_id) values (?, ?, 114)");
            PreparedStatement addEstimate = connection.prepareStatement("insert into PARAMS (number_value, object_id, attribute_id) values (?, ?, 115)");

            addObject.setLong(1, task.getTaskID());
            addObject.setString(2, task.getName());

            addName.setString(1, task.getName());
            addName.setLong(2, task.getTaskID());

            addEstimate.setLong(1, task.getEstimate());
            addEstimate.setLong(2, task.getTaskID());

            StringBuffer linkQuery = new StringBuffer();
            for(int i = 0; i < task.getEmployees().size(); i++){
                Employee employee = task.getEmployees().get(i);
                linkQuery.append("insert into LINKS (link_id, parent_id, child_id, link_type_id) values ("
                        + task.getTaskID()*employee.getID()%1273 +
                        "," + task.getTaskID() +
                        "," + employee.getID() + "155);");
            }

            PreparedStatement linksStatement = connection.prepareStatement(linkQuery.toString());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();
            int k = linksStatement.executeUpdate();

            if(i==1 && j==1 && k==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Task findTask(long key) {
        Task task = (Task) SingletonCache.getInstance().get(key);

        if(task != null)
            return task;

        Connection connection = oracleConnection.getConnection();
        List<Employee> employees = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            Statement employStatement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select attr.ATTRIBUTE_ID, o.object_id, p.text_value, p.NUMBER_VALUE\n" +
                    "from objects o\n" +
                    "inner join attributes attr on attr.type_id = o.TYPE_ID\n" +
                    "left join params p on p.ATTRIBUTE_ID = attr.ATTRIBUTE_ID\n" +
                    "and p.object_id = o.OBJECT_ID\n" +
                    "where o.object_id = " + key);

            ResultSet employSet = employStatement.executeQuery("SELECT E.OBJECT_ID, E.NAME\n" +
                    "   FROM Objects E\n" +
                    "    INNER JOIN LINKS L ON L.PARENT_ID = E.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS T ON L.CHILD_ID = T.OBJECT_ID\n" +
                    "    INNER JOIN TYPES OT ON T.TYPE_ID = OT.TYPE_ID\n" +
                    "    WHERE OT.TYPE_ID = 7\n" +
                    "    AND LT.LINK_TYPE_ID = 155\n" +
                    "    AND T.OBJECT_ID =" + key);

            task = extractTaskFromResultSet(resultSet);

            while(employSet.next())
                employees.add(oracleEmployeeDAO.findEmployee(employSet.getLong(1)));

            task.setEmployees(employees);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        SingletonCache.getInstance().put(key, task);
        return task;
    }

    @Override
    public boolean updateTask(Task task) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 110");
            PreparedStatement updateEstimate = connection.prepareStatement("update params set number_value = ? where object_id = ? and attribute_id = 110");

            updateName.setString(1, task.getName());
            updateName.setLong(2, task.getTaskID());

            updateEstimate.setLong(1, task.getEstimate());
            updateEstimate.setLong(2, task.getTaskID());

            int i = updateName.executeUpdate();
            int j = updateEstimate.executeUpdate();

            if(i==1 && j==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTask(long key) {
        Connection connection = oracleConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate("delete from params where object_id = " + key);
            int j = statement.executeUpdate("delete from objects where object_id = " + key);
            int k = statement.executeUpdate("delete from links where child_id = " + key);
            int s = statement.executeUpdate("delete from links where parent_id = " + key);

            if(i==1 && j==1 && k==1 && s==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
