package com.saasant.firstSpringProject.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/billingsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "@ppr@meY24";
    private static volatile DBUtil instance = new DBUtil();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC Driver", e);
        }
    }

    private DBUtil() {
        if (instance != null) {
            throw new IllegalStateException("Singleton already constructed");
        }
    }

    public static DBUtil getInstance() {
        return instance;
    }
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}