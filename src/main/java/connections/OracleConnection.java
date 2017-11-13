package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {
    private Connection connection;

    public Connection getConnection(){
        String userName = "OLGAKOZLOVA";
        String password = "qwerty";
        String connectionURL = "jdbc:oracle:thin:@localhost:1521:XE";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(connectionURL, userName, password);
            System.out.println("We're connected!");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
