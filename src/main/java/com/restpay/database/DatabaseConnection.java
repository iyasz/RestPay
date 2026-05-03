/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author iyasz
 */
public class DatabaseConnection {
    private static final String DB_NAME = "restpay.db";
    private static final String URL = "jdbc:sqlite:" + DB_NAME;
    
    private static Connection connection = null;
    
    public static Connection getConnection(){
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("Database terhubung: " + DB_NAME);
            }
        } catch (SQLException e) {
            System.out.println("Gagal koneksi: " + e.getMessage());
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal menutup koneksi: " + e.getMessage());
        }
    }
    
}
