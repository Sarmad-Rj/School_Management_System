package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xepdb1"; // Change if needed
    private static final String USER = "schooldb"; // Your Oracle username
    private static final String PASSWORD = "1234"; // Your Oracle password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

