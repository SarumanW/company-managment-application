import connections.OracleConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        OracleConnection oracleConnection = new OracleConnection();
        Connection connection = oracleConnection.getConnection();
        connection.close();
    }
}
