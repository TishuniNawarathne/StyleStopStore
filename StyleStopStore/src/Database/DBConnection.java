package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Database utility: provides a reusable method to obtain a MySQL connection
public class DBConnection {
    // JDBC URL: points to local MySQL 'stylestop' database
    private static final String URL = "jdbc:mysql://localhost:3306/stylestop";
    // Username: MySQL user
    private static final String USER = "root";
    // Password: MySQL password (empty by default)
    private static final String PASSWORD = "";

    // Factory method: returns a live JDBC Connection
    // Loads the MySQL driver and opens a connection using URL, USER, PASSWORD
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}
