package org.example.db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    Connection con = null;

    public DataBase() throws SQLException {
        con = getConnection();
//        Statement statement = con.createStatement();
//        String query = "CREATE DATABASE IF NOT EXISTS users;";
//        ResultSet rs = statement.executeQuery(query);
//        while (rs.next()) {
//            System.out.println(rs.getInt("column1") + " " + rs.getString("column2"));
//        }

    }

    public void addUser(String chatId) {
        String tableName = "table_" + chatId;
        try {
            String query = String.format("CREATE TABLE IF NOT EXISTS %s ( task VARCHAR(255) NOT NULL, date VARCHAR(255) NOT NULL, time VARCHAR(255) NOT NULL);", tableName);
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public void updateUser(String chatId, String task, String date, String time) {
        String tableName = "table_" + chatId;
        try {
            if (getUserSet(chatId) == null) {
                addUser(chatId);
            }
            String query = String.format("INSERT INTO %s (task, date, time) " +
                    "VALUES ('%s', '%s', '%s');", tableName, task, date, time);
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public ResultSet getUserSet(String chatId) {
        String tableName = "table_" + chatId;
        try {
            String query = String.format("SELECT * FROM %s", tableName);
            Statement statement = con.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public void clearTable(String chatId){
        String tableName = "table_" + chatId;
        try{
            String query = String.format("DELETE FROM %s;", tableName);
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e){
            System.out.println(e);
        }
    }

    public Map<String, Map<String, String>> getUserTask(String chatId) {
        try {
            ResultSet resultSet = getUserSet(chatId);
            Map<String, Map<String, String>> userTasks = new HashMap<String, Map<String, String>>();
            while (resultSet.next()) {
                String task = resultSet.getString("task");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                userTasks.put(task, Map.of(date, time));
            }
            return userTasks;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/users";
            String username = "root";
            String password = "qwerty";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return connection;
    }
}
