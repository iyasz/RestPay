/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.repositories;
import com.restpay.database.DatabaseConnection;
import com.restpay.models.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iyasz
 */
public class DashboardRepository {
    
    public static long getPendapatanHariIni() {
        String sql = """
            SELECT COALESCE(SUM(total), 0)
            FROM orders
            WHERE DATE(paid_at) = DATE('now', 'localtime')
            AND status = 'paid'
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (Exception e) {
            System.err.println("Gagal ambil pendapatan: " + e.getMessage());
            return 0;
        }
    }
    
    public static int getTransaksiHariIni() {
        String sql = """
            SELECT COUNT(*)
            FROM orders
            WHERE DATE(paid_at) = DATE('now', 'localtime')
            AND status = 'paid'
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            System.err.println("Gagal ambil transaksi: " + e.getMessage());
            return 0;
        }
    }
    
    public static int getTotalMenuTersedia() {
        String sql = """
            SELECT COUNT(*)
            FROM products
            WHERE is_available = 1
            AND deleted_at IS NULL
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            System.err.println("Gagal ambil menu: " + e.getMessage());
            return 0;
        }
    }
    
    public static List<Order> getRecentOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT invoice_number, customer_name, total, paid_at
            FROM orders
            ORDER BY created_at DESC
            LIMIT 5
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new Order(
                    rs.getString("invoice_number"),
                    rs.getString("customer_name"),
                    rs.getLong("total"),
                    rs.getString("paid_at")
                ));
            }
        } catch (Exception e) {
            System.err.println("Gagal ambil recent orders: " + e.getMessage());
        }
        return orders;
    }
    
}
