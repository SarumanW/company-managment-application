package dao.oracle_dao;

import caching.SingletonCache;
import connections.OracleConnection;
import dao.dao_interface.ProjectDAO;
import domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OracleProjectDAO implements ProjectDAO {
    private OracleConnection oracleConnection = new OracleConnection();
    private OracleManagerDAO oracleManagerDAO = new OracleManagerDAO();
    private OracleCustomerDAO oracleCustomerDAO = new OracleCustomerDAO();
    private OracleSprintDAO oracleSprintDAO = new OracleSprintDAO();

    private Project extractProjectFromResultSet(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setProjectID(resultSet.getLong(2));

        while(resultSet.next()){
            int i = resultSet.getInt(1);
            switch(i){
                case 110:
                    project.setName(resultSet.getString(3));
                    break;
                case 111:
                    project.setStart(resultSet.getDate(4));
                    break;
                case 112:
                    project.setEnd(resultSet.getDate(4));
                    break;
            }
        }

        return project;
    }

    @Override
    public boolean insertProject(Project project) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 5)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, number_value, object_id, attribute_id) values (?, NULL, ?, 110)");
            PreparedStatement addStart = connection.prepareStatement("insert into PARAMS (date_value, object_id, attribute_id) values (?, ?, 111)");
            PreparedStatement addEnd = connection.prepareStatement("insert into PARAMS (date_value, object_id, attribute_id) values (?, ?, 112)");
            PreparedStatement addManagerLink = connection.prepareStatement("insert into LINKS (link_id, parent_id, child_id, link_type_id) values (?, ?, ?, 151)");
            PreparedStatement addCustomerLink = connection.prepareStatement("insert into LINKS (link_id, parent_id, child_id, link_type_id) values (?, ?, ?, 152)");

            addObject.setLong(1, project.getProjectID());
            addObject.setString(2, project.getName());

            addName.setString(1, project.getName());
            addName.setLong(2, project.getProjectID());

            addStart.setDate(1, (Date) project.getStart().getTime());
            addStart.setLong(2, project.getProjectID());

            addEnd.setDate(1, (Date) project.getEnd().getTime());
            addEnd.setLong(2, project.getProjectID());

            addManagerLink.setLong(1, project.getProjectID()%13);
            addManagerLink.setLong(2, project.getManager().getID());
            addManagerLink.setLong(3, project.getProjectID());

            addCustomerLink.setLong(1, project.getProjectID()%17);
            addCustomerLink.setLong(2, project.getCustomer().getCustomerID());
            addCustomerLink.setLong(3, project.getProjectID());

            StringBuffer linkQuery = new StringBuffer();
            for(int i = 0; i < project.getSprints().size(); i++){
                Sprint sprint = project.getSprints().get(i);
                linkQuery.append("insert into LINKS (link_id, parent_id, child_id, link_type_id) values ("
                        + project.getProjectID()*project.getProjectID()%1273 +
                        "," + project.getProjectID() +
                        "," + sprint.getSprintID() + "153);");
            }

            PreparedStatement linksStatement = connection.prepareStatement(linkQuery.toString());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();
            int k = addStart.executeUpdate();
            int l = addEnd.executeUpdate();
            int s = addManagerLink.executeUpdate();
            int m = addCustomerLink.executeUpdate();
            int z = linksStatement.executeUpdate();

            if(i==1 && j==1 && k==1 && l==1 && s==1 && m==1 && z==1)
                return true;
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Project findProject(long key) {
        Project project = (Project) SingletonCache.getInstance().get(key);

        if(project != null)
            return project;

        Connection connection = oracleConnection.getConnection();
        Manager manager;
        Customer customer;
        List<Sprint> sprints = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            Statement managerStat = connection.createStatement();
            Statement customerStat = connection.createStatement();
            Statement sprintStat = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select attr.ATTRIBUTE_ID, o.object_id, p.text_value, p.DATE_VALUE\n" +
                    "from objects o\n" +
                    "inner join attributes attr on attr.type_id = o.TYPE_ID\n" +
                    "left join params p on p.ATTRIBUTE_ID = attr.ATTRIBUTE_ID\n" +
                    "and p.object_id = o.OBJECT_ID\n" +
                    "where o.object_id = " + key);

            ResultSet managSet = managerStat.executeQuery("SELECT M.OBJECT_ID, M.name\n" +
                    "   FROM Objects M\n" +
                    "    INNER JOIN LINKS L ON L.PARENT_ID = M.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS P ON L.CHILD_ID = P.OBJECT_ID\n" +
                    "    INNER JOIN TYPES PT ON P.TYPE_ID = PT.TYPE_ID\n" +
                    "    WHERE PT.TYPE_ID = 5\n" +
                    "    AND LT.LINK_TYPE_ID = 151\n" +
                    "    AND P.OBJECT_ID = " + key);

            ResultSet custSet = customerStat.executeQuery("SELECT C.OBJECT_ID, C.name\n" +
                    "   FROM Objects C\n" +
                    "    INNER JOIN LINKS L ON L.PARENT_ID = C.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS P ON L.CHILD_ID = P.OBJECT_ID\n" +
                    "    INNER JOIN TYPES PT ON P.TYPE_ID = PT.TYPE_ID\n" +
                    "    WHERE PT.TYPE_ID = 5\n" +
                    "    AND LT.LINK_TYPE_ID = 152\n" +
                    "    AND P.OBJECT_ID = " + key);

            ResultSet sprintSet = sprintStat.executeQuery("SELECT S.OBJECT_ID, S.name\n" +
                    "   FROM Objects S\n" +
                    "    INNER JOIN LINKS L ON L.CHILD_ID = S.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS P ON L.PARENT_ID = P.OBJECT_ID\n" +
                    "    INNER JOIN TYPES PT ON P.TYPE_ID = PT.TYPE_ID\n" +
                    "    WHERE PT.TYPE_ID = 5\n" +
                    "    AND LT.LINK_TYPE_ID = 153\n" +
                    "    AND P.OBJECT_ID = " + key);

            project = extractProjectFromResultSet(resultSet);
            manager = oracleManagerDAO.findManager(managSet.getLong(1));
            customer = oracleCustomerDAO.findCustomer(custSet.getLong(1));
            project.setManager(manager);
            project.setCustomer(customer);

            while(sprintSet.next())
                sprints.add(oracleSprintDAO.findSprint(sprintSet.getLong(1)));

            project.setSprints(sprints);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        SingletonCache.getInstance().put(key, project);
        return project;
    }

    @Override
    public boolean updateProject(Project project) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 110");
            PreparedStatement updateStart = connection.prepareStatement("update params set date_value = ? where object_id = ? and attribute_id = 111");
            PreparedStatement updateEnd = connection.prepareStatement("update params set date_value = ? where object_id = ? and attribute_id = 112");
            PreparedStatement updateManager = connection.prepareStatement("update links set parent_id = ? where child_id = ? and link_type_id = 151");
            PreparedStatement updateCustomer = connection.prepareStatement("update links set parent_id = ? where child_id = ? and link_type_id = 152");

            updateName.setString(1, project.getName());
            updateName.setLong(2, project.getProjectID());

            updateStart.setDate(1, (Date) project.getStart().getTime());
            updateStart.setLong(2, project.getProjectID());

            updateEnd.setDate(1, (Date) project.getEnd().getTime());
            updateEnd.setLong(2, project.getProjectID());

            updateManager.setLong(1, project.getManager().getID());
            updateManager.setLong(2, project.getProjectID());

            updateCustomer.setLong(1, project.getCustomer().getCustomerID());
            updateCustomer.setLong(2, project.getProjectID());

            int i = updateName.executeUpdate();
            int j = updateStart.executeUpdate();
            int k = updateEnd.executeUpdate();
            int s = updateManager.executeUpdate();
            int l = updateCustomer.executeUpdate();

            if(i==1 && j==1 && k==1 && s==1 && l==1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

       return false;
    }

    @Override
    public boolean deleteProject(long key) {
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
