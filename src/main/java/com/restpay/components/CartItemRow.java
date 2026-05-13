/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.components;

import com.restpay.models.CartItem;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author iyasz
 */
public class CartItemRow extends JPanel {
    private JLabel lblQuantity;
    private Runnable onChange; // callback ke Cashier untuk update total
    
    public CartItemRow(CartItem item, Runnable onChange) {
        this.onChange = onChange;
        initRow(item);
    }
    
    private void initRow(CartItem item) {
        setLayout(new BorderLayout(8, 0));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // Nama produk
        JLabel lblName = new JLabel(item.getProduct().getName());
        lblName.setFont(new Font("Arial", Font.PLAIN, 13));

        // Subtotal — deklarasi duluan agar bisa dipakai di listener bawah
        JLabel lblSubtotal = new JLabel(String.format("Rp %,d", item.getSubtotal()));
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 13));
        lblSubtotal.setForeground(new Color(102, 102, 255));

        // Kontrol qty
        JPanel pnlQty = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        pnlQty.setBackground(Color.WHITE);

        JButton btnMinus = new JButton("-");
        btnMinus.setPreferredSize(new Dimension(42, 42));
        btnMinus.setBackground(new Color(254, 254, 254));
        btnMinus.setFocusPainted(false);

        lblQuantity = new JLabel(String.valueOf(item.getQuantity()));
        lblQuantity.setFont(new Font("Arial", Font.BOLD, 13));
        lblQuantity.setPreferredSize(new Dimension(24, 28));
        lblQuantity.setHorizontalAlignment(JLabel.CENTER);

        JButton btnPlus = new JButton("+");
        btnPlus.setPreferredSize(new Dimension(42, 42));
        btnPlus.setBackground(new Color(254, 254, 254));
        btnPlus.setFocusPainted(false);

        // ✅ 1 listener saja, semua update sekaligus
        btnMinus.addActionListener(e -> {
            item.decrementQuantity();
            lblQuantity.setText(String.valueOf(item.getQuantity()));
            lblSubtotal.setText(String.format("Rp %,d", item.getSubtotal())); // ← langsung update
            onChange.run();
        });

        btnPlus.addActionListener(e -> {
            item.incrementQuantity();
            lblQuantity.setText(String.valueOf(item.getQuantity()));
            lblSubtotal.setText(String.format("Rp %,d", item.getSubtotal())); // ← langsung update
            onChange.run();
        });

        pnlQty.add(btnMinus);
        pnlQty.add(lblQuantity);
        pnlQty.add(btnPlus);

        add(lblName,     BorderLayout.WEST);
        add(pnlQty,      BorderLayout.CENTER);
        add(lblSubtotal, BorderLayout.EAST);
    }
    
}
