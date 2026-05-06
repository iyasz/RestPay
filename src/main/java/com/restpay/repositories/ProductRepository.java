/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.repositories;
import com.restpay.database.DatabaseConnection;
import com.restpay.models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author iyasz
 */
public class ProductRepository {
    public static boolean save(Product product) {
        String sql = """
            INSERT INTO products (category_id, name, description, price, is_available)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt   (1, product.getCategoryId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setLong(4, product.getPrice());
            stmt.setInt   (5, product.isAvailable() ? 1 : 0);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("Gagal simpan produk: " + e.getMessage());
            return false;
        }
        
    }
}
