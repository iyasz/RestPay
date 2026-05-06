/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.repositories;
import com.restpay.database.DatabaseConnection;
import com.restpay.models.Category;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iyasz
 */
public class CategoryRepository {
    public static List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        
        String sql = "SELECT id, name FROM categories ORDER BY name ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Category(
                rs.getInt("id"),
                rs.getString("name")
                ));
            }
        } catch (Exception e) {
            System.err.println("Gagal ambil kategori: " + e.getMessage());
        }

        return categories;
    }
}
