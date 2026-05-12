/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.repositories;
import com.restpay.database.DatabaseConnection;
import com.restpay.models.Product;
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

public class ProductRepository {

    public static List<Product> getAll() {
        List<Product> products = new ArrayList<>();

        // JOIN ke categories untuk ambil nama kategori sekalian
        String sql = """
            SELECT p.id, p.category_id, c.name as category_name,
                   p.name, p.description, p.price, p.is_available
            FROM products p
            JOIN categories c ON p.category_id = c.id
            WHERE p.deleted_at IS NULL
            ORDER BY p.created_at DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("id"),
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getLong("price"),
                    rs.getInt("is_available") == 1
                ));
            }

        } catch (Exception e) {
            System.err.println("Gagal ambil produk: " + e.getMessage());
        }

        return products;
    }
    
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
    
    public static boolean update(Product product) {
        String sql = """
            UPDATE products
            SET category_id  = ?,
                name         = ?,
                description  = ?,
                price        = ?,
                is_available = ?,
                updated_at   = datetime('now', 'localtime')
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt   (1, product.getCategoryId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setLong  (4, product.getPrice());
            stmt.setInt   (5, product.isAvailable() ? 1 : 0);
            stmt.setInt   (6, product.getId()); // ← WHERE id = ?

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("Gagal update produk: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean delete(int id) {
        // Soft delete — isi deleted_at, data tetap ada di database
        String sql = "UPDATE products SET deleted_at = datetime('now', 'localtime') WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("Gagal hapus produk: " + e.getMessage());
            return false;
        }
    }
    
}
