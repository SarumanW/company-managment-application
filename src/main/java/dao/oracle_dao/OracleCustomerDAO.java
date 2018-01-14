package dao.oracle_dao;

import caching.SingletonCache;
import connections.OracleConnection;
import dao.dao_interface.CustomerDAO;
import domain.Customer;
import domain.Project;
import generator.UniqueID;

import java.sql.*;

public class OracleCustomerDAO implements CustomerDAO {
    private OracleConnection oracleConnection = new OracleConnection();

    private Customer extractCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();

        while(resultSet.next()) {
            int i = resultSet.getInt(1);
            switch (i) {
                case 105:
                    customer.setName(resultSet.getString(3));
                    break;
                case 106:
                    customer.setSurname(resultSet.getString(3));
                    break;
            }
        }

        return customer;
    }

    @Override
    public boolean insertCustomer(Customer customer) {
        Connection connection = oracleConnection.getConnection();

        try{
            PreparedStatement addObject = connection.prepareStatement("insert into OBJECTS (OBJECT_ID, NAME, TYPE_ID) values (?, ?, 3)");
            PreparedStatement addName = connection.prepareStatement("insert into PARAMS (text_value, object_id, attribute_id) values (?, ?, 105)");
            PreparedStatement addSurname = connection.prepareStatement("insert into PARAMS (text_value,object_id, attribute_id) values (?, ?, 106)");
            PreparedStatement addProjectLink = connection.prepareStatement("insert into LINKS (link_id, parent_id, child_id, link_type_id) values (?, ?, ?, 152)");

            addObject.setLong(1, customer.getCustomerID());
            addObject.setString(2, customer.getName());

            addName.setLong(2, customer.getCustomerID());
            addName.setString(1, customer.getName());

            addSurname.setString(1, customer.getSurname());
            addSurname.setLong(2, customer.getCustomerID());

            addProjectLink.setLong(1, UniqueID.generateID(new Object()));
            addProjectLink.setLong(2, customer.getCustomerID());
            addProjectLink.setLong(3, customer.getProjectID());

            int i = addObject.executeUpdate();
            int j = addName.executeUpdate();
            int k = addSurname.executeUpdate();
            int l = addProjectLink.executeUpdate();

            if(i==1 && j==1 && k==1 && l==1)
                return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Customer findCustomer(long key) {
        Customer customer = (Customer) SingletonCache.getInstance().get(key);

        if(customer != null)
            return customer;

        Connection connection = oracleConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            Statement projectStat = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select attr.ATTRIBUTE_ID, o.object_id, p.text_value\n" +
                    "from objects o\n" +
                    "inner join attributes attr on attr.type_id = o.TYPE_ID\n" +
                    "left join params p on p.ATTRIBUTE_ID = attr.ATTRIBUTE_ID\n" +
                    "and p.object_id = o.OBJECT_ID\n" +
                    "where o.object_id = " + key);

            ResultSet projectSet = projectStat.executeQuery("SELECT P.OBJECT_ID, P.name\n" +
                    "   FROM Objects P\n" +
                    "    INNER JOIN LINKS L ON L.CHILD_ID = P.OBJECT_ID\n" +
                    "    INNER JOIN LINKTYPES LT ON L.LINK_TYPE_ID = LT.LINK_TYPE_ID\n" +
                    "    INNER JOIN OBJECTS C ON L.PARENT_ID = C.OBJECT_ID\n" +
                    "    INNER JOIN TYPES CT ON C.TYPE_ID = CT.TYPE_ID\n" +
                    "    WHERE CT.TYPE_ID = 3\n" +
                    "    AND LT.LINK_TYPE_ID = 152\n" +
                    "    AND C.OBJECT_ID = " + key);

            customer = extractCustomerFromResultSet(resultSet);
            customer.setCustomerID(key);
            projectSet.next();
            customer.setProject(projectSet.getLong(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        Connection connection = oracleConnection.getConnection();

        try {
            PreparedStatement updateName = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 105");
            PreparedStatement updateSurname = connection.prepareStatement("update params set text_value = ? where object_id = ? and attribute_id = 106");
            PreparedStatement updateProject = connection.prepareStatement("update links set parent_id = ? where child_id = ? and link_type_id = 152");

            updateName.setString(1, customer.getName());
            updateName.setLong(2, customer.getCustomerID());

            updateSurname.setString(1, customer.getSurname());
            updateSurname.setLong(2, customer.getCustomerID());

            updateProject.setLong(2, customer.getProjectID());
            updateProject.setLong(1, customer.getCustomerID());

            int i = updateName.executeUpdate();
            int j = updateSurname.executeUpdate();
            int k = updateProject.executeUpdate();

            if(i==1 && j==1 && k==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(long key) {
        Connection connection = oracleConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate("delete from params where object_id = " + key);
            int j = statement.executeUpdate("delete from objects where object_id = " + key);
            int s = statement.executeUpdate("delete from links where parent_id = " + key);

            if(i==1 && j==1 && s==1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
