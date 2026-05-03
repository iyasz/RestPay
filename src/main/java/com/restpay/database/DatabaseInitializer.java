/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author iyasz
 */
public class DatabaseInitializer {
    
    public static void initialize() {
        
        Connection conn = DatabaseConnection.getConnection();
        
        try (Statement stmt = conn.createStatement()){
            
            // Aktifkan foreign key di SQLite (default mati)
            stmt.execute("PRAGMA foreign_keys = ON");
            
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categories (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    name        TEXT    NOT NULL,
                    created_at  TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at  TEXT    DEFAULT (datetime('now', 'localtime')),
                    deleted_at  TEXT    DEFAULT NULL
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    category_id  INTEGER NOT NULL,
                    image        TEXT    DEFAULT NULL,
                    name         TEXT    NOT NULL,
                    description  TEXT    DEFAULT NULL,
                    price        REAL    NOT NULL,
                    is_available INTEGER DEFAULT 1,
                    created_at   TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at   TEXT    DEFAULT (datetime('now', 'localtime')),
                    deleted_at   TEXT    DEFAULT NULL,
                    FOREIGN KEY (category_id) REFERENCES categories(id)
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS product_option_groups (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    product_id      INTEGER NOT NULL,
                    name            TEXT    NOT NULL,
                    is_required     INTEGER DEFAULT 0,
                    max_selections  INTEGER DEFAULT 1,
                    created_at      TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at      TEXT    DEFAULT (datetime('now', 'localtime')),
                    deleted_at      TEXT    DEFAULT NULL,
                    FOREIGN KEY (product_id) REFERENCES products(id)
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS product_details (
                    id                INTEGER PRIMARY KEY AUTOINCREMENT,
                    option_group_id   INTEGER NOT NULL,
                    name              TEXT    NOT NULL,
                    adjustment_amount REAL    DEFAULT 0,
                    is_available      INTEGER DEFAULT 1,
                    created_at        TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at        TEXT    DEFAULT (datetime('now', 'localtime')),
                    deleted_at        TEXT    DEFAULT NULL,
                    FOREIGN KEY (option_group_id) REFERENCES product_option_groups(id)
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    invoice_number  TEXT    NOT NULL UNIQUE,
                    customer_name   TEXT    DEFAULT NULL,
                    subtotal        REAL    NOT NULL DEFAULT 0,
                    service_charge  REAL    NOT NULL DEFAULT 0,
                    restaurant_tax  REAL    NOT NULL DEFAULT 0,
                    total           REAL    NOT NULL DEFAULT 0,
                    status          TEXT    NOT NULL DEFAULT 'belum dibayar',
                    payment_method  TEXT    DEFAULT NULL,
                    cash_amount     REAL    DEFAULT 0,
                    change          REAL    DEFAULT 0,
                    paid_at         TEXT    DEFAULT NULL,
                    created_at      TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at      TEXT    DEFAULT (datetime('now', 'localtime'))
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_details (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_id      INTEGER NOT NULL,
                    product_id    INTEGER NOT NULL,
                    product_name  TEXT    NOT NULL,
                    unit_price    REAL    NOT NULL,
                    qty           INTEGER NOT NULL DEFAULT 1,
                    subtotal      REAL    NOT NULL,
                    created_at    TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at    TEXT    DEFAULT (datetime('now', 'localtime')),
                    FOREIGN KEY (order_id)   REFERENCES orders(id),
                    FOREIGN KEY (product_id) REFERENCES products(id)
                )
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_detail_options (
                    id                INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_detail_id   INTEGER NOT NULL,
                    product_option_id INTEGER NOT NULL,
                    option_name       TEXT    NOT NULL,
                    adjustment_amount REAL    DEFAULT 0,
                    created_at        TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at        TEXT    DEFAULT (datetime('now', 'localtime')),
                    FOREIGN KEY (order_detail_id)   REFERENCES order_details(id),
                    FOREIGN KEY (product_option_id) REFERENCES product_details(id)
                )
            """);
            

            System.out.println("✅ Tabel berhasil dibuat/sudah ada.");
    
        } catch (SQLException e) {
            System.out.println("❌ Gagal inisialisasi tabel: " + e.getMessage());
        }
    }
}
