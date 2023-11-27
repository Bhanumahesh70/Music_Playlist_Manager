package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static Connection connection;

    static {
        try {
            System.out.println("Connecting to the database");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://www.papademas.net:3307/510fp?autoReconnect=true&useSSL=false",
                    "fp510",
                    "510");
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Error creating connection to the database: " + e);
            System.exit(-1);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
