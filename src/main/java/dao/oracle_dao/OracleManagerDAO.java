package dao.oracle_dao;

import caching.SingletonCache;
import connections.OracleConnection;
import dao.dao_interface.ManagerDAO;
import domain.Manager;
import domain.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleManagerDAO implements ManagerDAO {
    private OracleConnection oracleConnection = new OracleConnection();
    private OracleProjectDAO oracleProjectDAO = new OracleProjectDAO();

    private Manager extractManagerFromResultSet(ResultSet resultSet) throws SQLException {
        Manager manager = new Manager();
        manager.setID(resultSet.getLong(2));

        while(resultSet.next()){
            int i = resultSet.getInt(1);
            switch (i){
                case 107:
                    manager.setName(resultSet.getString(3));
                    break;
                case 108:
                    manager.setSurname(resultSet.getString(3));
                    break;
                case 109:
                    manager.setSalary(resultSet.getDouble(4));
                    break;
            }
        }
        return manager;
    }

    public boolean insertManager(Manager manager) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 4)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 107)");
            PreparedStatement addSurname = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 108)");
            PreparedStatement addSalary = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (NULL, ?, ?, 109)");

            addObject.setLong(1, manager.getID());
            addObject.setString(2, manager.getSurname());

            addName.setString(1, manager.getName());
            addName.setLong(2, manager.getID());

            addSurname.setString(1, manager.getSurname());
            addSurname.setLong(2, manager.getID());

            addSalary.setDouble(1,manager.getSalary());
            addSalary.setLong(2, manager.getID());

            StringBuffer linkQuery = new StringBuffer();
            for(int i = 0; i < manager.getProjectList().size(); i++){
                Project project = manager.getProjectList().get(i);
                linkQuery.append("insert into LINKS (link_id, parent_id, child_id, link_type_id) values ("
                        + project.getProjectID()*manager.getID()%1273 +
                        "," + manager.getID() +
                        "," + project.getProjectID() + "151);");
            }

            PreparedStatement linksStatement = connection.prepareStatement(linkQuery.toString());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();
            int k = addSurname.executeUpdate();
            int z = addSalary.executeUpdate();
            int m = linksStatement.executeUpdate();

            if(i==1 && j==1 && k==1 && z==1 && m==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Manager findManager(long key) {
        Manager manager = (Manager) SingletonCache.getInstance().get(key);

        if(manager!=null)
            return manager;

        Connection connection = oracleConnection.getConnection();
        List<Project> projects = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            Statement projectStatement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select attr.ATTRIBUTE_ID, o.object_id, p.text_value, p.NUMBER_VALUE\n" +
                    "from objects o\n" +
                    "inner join attributes attr on attr.type_id = o.TYPE_ID\n" +
                    "left join params p on p.ATTRIBUTE_ID = attr.ATTRIBUTE_ID\n" +
                    "and p.object_id = o.OBJECT_ID\n" +
                    "where o.object_id = " + key);

            ResultSet projectSet = projectStatement.executeQuery("SELECT P.OBJECT_ID, P.NAME\n" +
                    "   FROM Objects P\n" +
                    "    INNER JOIN LINKS L ON L.CHILD_ID = P.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS M ON L.PARENT_ID = M.OBJECT_ID\n" +
                    "    INNER JOIN TYPES OT ON M.TYPE_ID = OT.TYPE_ID\n" +
                    "    WHERE OT.TYPE_ID = 4\n" +
                    "    AND LT.LINK_TYPE_ID = 151\n" +
                    "    AND M.OBJECT_ID = " + key);

            manager = extractManagerFromResultSet(resultSet);

            while(projectSet.next()){
                Project project = oracleProjectDAO.findProject(projectSet.getLong(1));
                projects.add(project);
            }
            manager.setProjectList(projects);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        SingletonCache.getInstance().put(key, manager);
        return manager;
    }

    public boolean updateManager(Manager manager) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 107");
            PreparedStatement updateSurname = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 108");
            PreparedStatement updateSalary = connection.prepareStatement("update params set number_value = ? where object_id = ? and attribute_id = 109");

            updateName.setString(1, manager.getName());
            updateName.setLong(2, manager.getID());

            updateSurname.setString(1, manager.getSurname());
            updateSurname.setLong(2, manager.getID());

            updateSalary.setDouble(1, manager.getSalary());
            updateSalary.setLong(2, manager.getID());

            int i = updateName.executeUpdate();
            int j = updateSurname.executeUpdate();
            int z = updateSalary.executeUpdate();

            if(i==1 && j==1 && z==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteManager(long key) {
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
