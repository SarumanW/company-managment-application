import connections.OracleConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        OracleConnection oracleConnection = new OracleConnection();
        Connection connection = oracleConnection.getConnection();

        if(connection != null) try {
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
