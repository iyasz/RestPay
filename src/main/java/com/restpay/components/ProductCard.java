/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.components;

import com.restpay.models.Product;
import com.restpay.repositories.ProductRepository;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author iyasz
 */
public class ProductCard extends JPanel {
    
    private Runnable onDeleted;
    private java.util.function.Consumer<Product> onEdit;
    
    
    public ProductCard(Product product) {
        initCard(product);
    }
    
    // Update constructor
    public ProductCard(Product product, Runnable onDeleted, java.util.function.Consumer<Product> onEdit) {
        this.onDeleted = onDeleted;
        this.onEdit    = onEdit;   // ← tambah ini
        initCard(product);
    }

    private void initCard(Product product) {
        setPreferredSize(new Dimension(200, 240));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(12, 12, 12, 12)
        ));
        setLayout(new BorderLayout(0, 8));

        // === NORTH: Placeholder gambar ===
        JLabel lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(176, 90));
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(240, 240, 240));
        lblImage.setText("No Image");
        lblImage.setForeground(new Color(180, 180, 180));
        add(lblImage, BorderLayout.NORTH);

        // === CENTER: Nama & Kategori ===
        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setBackground(Color.WHITE);

        JLabel lblName = new JLabel(product.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        lblName.setForeground(new Color(33, 33, 33));

        JLabel lblCategory = new JLabel(product.getCategoryName());
        lblCategory.setFont(new Font("Arial", Font.PLAIN, 11));
        lblCategory.setForeground(new Color(120, 120, 120));

        pnlInfo.add(lblName);
        pnlInfo.add(Box.createVerticalStrut(4));
        pnlInfo.add(lblCategory);
        add(pnlInfo, BorderLayout.CENTER);

        // === SOUTH: Harga + Status + Tombol ===
        JPanel pnlBottom = new JPanel(new BorderLayout(0, 6));
        pnlBottom.setBackground(Color.WHITE);

        // Baris 1: Harga & Status
        JPanel pnlPriceStatus = new JPanel(new BorderLayout());
        pnlPriceStatus.setBackground(Color.WHITE);

        JLabel lblPrice = new JLabel("Rp " + String.format("%,d", product.getPrice()));
        lblPrice.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrice.setForeground(new Color(102, 102, 255));

        JLabel lblStatus = new JLabel(product.isAvailable() ? "● Tersedia" : "● Habis");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(product.isAvailable() ? new Color(0, 180, 0) : Color.RED);

        pnlPriceStatus.add(lblPrice, BorderLayout.WEST);
        pnlPriceStatus.add(lblStatus, BorderLayout.EAST);

        // Baris 2: Tombol Edit & Hapus
        JPanel pnlButtons = new JPanel(new GridLayout(1, 2, 6, 0));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBackground(new Color(255, 193, 7));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("Arial", Font.BOLD, 11));
        btnEdit.setBorder(null);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> {
            if (onEdit != null) onEdit.accept(product);
        });

        JButton btnDelete = new JButton("Hapus");
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Arial", Font.BOLD, 11));
        btnDelete.setBorder(null);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> handleDelete(product));

        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);

        pnlBottom.add(pnlPriceStatus, BorderLayout.NORTH);
        pnlBottom.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlBottom, BorderLayout.SOUTH); // ← ini yang sebelumnya hilang!
    }
    
    private void handleDelete(Product product) {
        // Konfirmasi dulu sebelum hapus
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Hapus \"" + product.getName() + "\"?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean success = ProductRepository.delete(product.getId());

        if (success) {
            // Panggil callback → Menu akan refresh otomatis
            if (onDeleted != null) {
                onDeleted.run();
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Gagal menghapus produk!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
