/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.components;

import com.restpay.models.Product;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author iyasz
 */
public class ProductCard extends JPanel {
    public ProductCard(Product product) {
        initCard(product);
    }
    
    private void initCard(Product product) {
        // Ukuran tiap kartu
        setPreferredSize(new Dimension(200, 220));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(12, 12, 12, 12)
        ));
        setLayout(new BorderLayout(0, 8));

        // === Bagian Atas: Gambar / Placeholder ===
        JLabel lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(176, 110));
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(240, 240, 240));
        lblImage.setText("No Image");
        lblImage.setForeground(new Color(180, 180, 180));
        add(lblImage, BorderLayout.NORTH);

        // === Bagian Tengah: Nama & Kategori ===
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

        // === Bagian Bawah: Harga & Status ===
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(Color.WHITE);

        JLabel lblPrice = new JLabel("Rp " + String.format("%,d", product.getPrice()));
        lblPrice.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrice.setForeground(new Color(102, 102, 255));

        JLabel lblStatus = new JLabel(product.isAvailable() ? "● Tersedia" : "● Habis");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(product.isAvailable() ? new Color(0, 180, 0) : Color.RED);

        pnlBottom.add(lblPrice, BorderLayout.WEST);
        pnlBottom.add(lblStatus, BorderLayout.EAST);
        add(pnlBottom, BorderLayout.SOUTH);
    }
}
