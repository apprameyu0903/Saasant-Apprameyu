package com.saasant.firstSpringProject.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {
	private static final Logger log = LoggerFactory.getLogger(DBUtil.class);
    private static final String URL = "jdbc:mysql://localhost:3306/billingsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "@ppr@meY24";
    private static volatile DBUtil instance = new DBUtil();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            log.info("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
        	log.error("Failed to load MySQL JDBC Driver", e);
            throw new RuntimeException("Failed to load MySQL JDBC Driver", e);
        }
    }

    private DBUtil() {
        if (instance != null) {
        	log.warn("DBUtil singleton constructor called when instance already exists.");
            throw new IllegalStateException("Singleton already constructed");
        }
        log.debug("DBUtil instance created.");
    }

    public static DBUtil getInstance() {
        return instance;
    }
    public Connection getConnection() throws SQLException {
    	log.debug("Attempting to establish database connection to URL: {}", URL);
    	try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            log.info("Database connection established successfully to URL: {}", URL);
            return connection;
        } catch (SQLException e) {
            log.error("Failed to establish database connection to URL: {}. User: {}", URL, USER, e);
            throw e;
        }
    }
}