package com.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
    private static Connection connection = null;
    
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        
        try {
            // Load database properties
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/resources/db.properties"));
            
            // Load the JDBC driver
            Class.forName(props.getProperty("DB_DRIVER"));
            
            // Create the connection
            connection = DriverManager.getConnection(
                props.getProperty("DB_URL"),
                props.getProperty("DB_USERNAME"),
                props.getProperty("DB_PASSWORD")
            );
            
            System.out.println("Database connection established successfully!");
            
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
        
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
