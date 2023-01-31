package clontz.clientschedulingapp.DataService;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DBConnector {
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/client_schedule?connectionTimeZone=SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String username = "sqlUser";
    private static final String password = "passw0rd!";
    public static Connection connection;

    public static void openConnection() {
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (Exception e) {
            System.out.println("Database Connection Open Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch(Exception e) {
            System.out.println("Database Connection Close Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
