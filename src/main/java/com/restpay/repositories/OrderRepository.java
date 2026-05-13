/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.repositories;

import com.restpay.database.DatabaseConnection;
import com.restpay.models.CartItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author iyasz
 */
public class OrderRepository {
    
    public static String save(String customerName, String notes, Map<Integer, CartItem> cart) {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // ← mulai transaksi

            // 1. Hitung total
            long total = cart.values().stream()
                .filter(item -> item.getQuantity() > 0)
                .mapToLong(CartItem::getSubtotal)
                .sum();

            // 2. Generate invoice number
            String invoiceNumber = generateInvoiceNumber(conn);

            // 3. Simpan ke tabel orders
            String sqlOrder = """
                INSERT INTO orders (invoice_number, customer_name, total, status, notes, paid_at)
                VALUES (?, ?, ?, 'paid', ?, datetime('now', 'localtime'))
            """;

            int orderId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, invoiceNumber);
                stmt.setString(2, customerName.isEmpty() ? null : customerName);
                stmt.setLong  (3, total);
                stmt.setString(4, notes.isEmpty() ? null : notes);
                stmt.executeUpdate();

                // Ambil id order yang baru dibuat
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                orderId = rs.getInt(1);
            }

            // 4. Simpan tiap item ke tabel order_details
            String sqlDetail = """
                INSERT INTO order_details (order_id, product_id, product_name, unit_price, qty, subtotal)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            try (PreparedStatement stmt = conn.prepareStatement(sqlDetail)) {
                for (CartItem item : cart.values()) {
                    if (item.getQuantity() <= 0) continue;

                    stmt.setInt   (1, orderId);
                    stmt.setInt   (2, item.getProduct().getId());
                    stmt.setString(3, item.getProduct().getName());
                    stmt.setLong  (4, item.getProduct().getPrice());
                    stmt.setInt   (5, item.getQuantity());
                    stmt.setLong  (6, item.getSubtotal());
                    stmt.addBatch(); // ← kumpulkan dulu, eksekusi sekaligus
                }
                stmt.executeBatch();
            }

            conn.commit(); // ← semua berhasil, simpan permanen
            return invoiceNumber; // return invoice sebagai tanda sukses

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback(); // ← ada error, batalkan semua
            } catch (SQLException ignored) {}
            System.err.println("Gagal simpan order: " + e.getMessage());
            return null;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ignored) {}
        }
    }
    
    // Generate nomor invoice otomatis: INV-20250514-001
    private static String generateInvoiceNumber(Connection conn) throws Exception {
        String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        String prefix = "INV-" + today + "-";

        String sql = "SELECT COUNT(*) FROM orders WHERE invoice_number LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prefix + "%");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1) + 1;
            return prefix + String.format("%03d", count); // INV-20250514-001
        }
    }
    
}
