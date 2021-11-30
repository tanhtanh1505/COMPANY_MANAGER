package com.companymanager;

import java.sql.*;

public class DatabaseManager {
    private static final String url = "jdbc:mysql://localhost:3307/company";

    private static Connection con;
    private static Statement statement;
    private static boolean isConnected = false;

    public static void connect(){
        try {
            con = DriverManager.getConnection(url, "root", "");
            statement = con.createStatement();
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    public static ResultSet getTable(String nameTable){
        try {
            return statement.executeQuery("select * from " + nameTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static ResultSet executeQuery(String query){
        try {
            return statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void executeUpdate(String query){
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
