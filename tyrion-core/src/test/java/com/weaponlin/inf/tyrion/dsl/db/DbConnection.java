package com.weaponlin.inf.tyrion.dsl.db;

import java.sql.Connection;
import java.sql.DriverManager;

class DbConnection {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/demo";

    static final String USER = "root";
    static final String PASS = "weaponlin";


    static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("get connection failed");
        }
    }
}
