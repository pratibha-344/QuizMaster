package com.quizmaster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String DEFAULT_URL =
            "jdbc:mysql://127.0.0.1:3306/quizmaster?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private Database() {}


public static Connection getConnection() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        throw new SQLException("MySQL JDBC Driver not found", e);
    }

    String url = System.getenv().getOrDefault("JDBC_URL", DEFAULT_URL);
    String user = System.getenv().getOrDefault("DB_USER", "root");
    String password = System.getenv().getOrDefault("DB_PASSWORD", "");

    return DriverManager.getConnection(url, user, password);
}
}
