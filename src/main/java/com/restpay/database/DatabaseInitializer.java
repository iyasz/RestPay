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

        try (Statement stmt = conn.createStatement()) {

            // Aktifkan foreign key di SQLite (default mati)
            stmt.execute("PRAGMA foreign_keys = ON");

            // Tabel categories
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categories (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    name        TEXT    NOT NULL,
                    created_at  TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at  TEXT    DEFAULT (datetime('now', 'localtime')),
                    deleted_at  TEXT    DEFAULT NULL
                )
            """);

            // Tabel products
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    category_id  INTEGER NOT NULL,
                    name         TEXT    NOT NULL,
                    description  TEXT    DEFAULT NULL,
                    price        INTEGER NOT NULL,
                    is_available INTEGER DEFAULT 1,
                    created_at   TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at   TEXT    DEFAULT (datetime('now', 'localtime')),
                    deleted_at   TEXT    DEFAULT NULL,
                    FOREIGN KEY (category_id) REFERENCES categories(id)
                )
            """);

            // Tabel orders
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id              INTEGER PRIMARY KEY AUTOINCREMENT,
                    invoice_number  TEXT    NOT NULL UNIQUE,
                    customer_name   TEXT    DEFAULT NULL,
                    total           INTEGER NOT NULL DEFAULT 0,
                    status          TEXT    NOT NULL DEFAULT 'paid',
                    notes           TEXT    DEFAULT NULL,
                    paid_at         TEXT    DEFAULT NULL,
                    created_at      TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at      TEXT    DEFAULT (datetime('now', 'localtime'))
                )
            """);

            // Tabel order_details
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_details (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_id      INTEGER NOT NULL,
                    product_id    INTEGER NOT NULL,
                    product_name  TEXT    NOT NULL,
                    unit_price    INTEGER NOT NULL,
                    qty           INTEGER NOT NULL DEFAULT 1,
                    subtotal      INTEGER NOT NULL,
                    created_at    TEXT    DEFAULT (datetime('now', 'localtime')),
                    updated_at    TEXT    DEFAULT (datetime('now', 'localtime')),
                    FOREIGN KEY (order_id)   REFERENCES orders(id),
                    FOREIGN KEY (product_id) REFERENCES products(id)
                )
            """);

            System.out.println("✅ Tabel berhasil dibuat/sudah ada.");

        } catch (SQLException e) {
            System.out.println("❌ Gagal inisialisasi tabel: " + e.getMessage());
        }
    }
}
