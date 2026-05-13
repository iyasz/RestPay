/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.restpay.pages;

import com.restpay.Navigable;
import com.restpay.components.CartItemRow;
import com.restpay.models.CartItem;
import com.restpay.models.Category;
import com.restpay.models.Product;
import com.restpay.repositories.CategoryRepository;
import com.restpay.repositories.OrderRepository;
import com.restpay.repositories.ProductRepository;
import com.restpay.utils.WrapLayout;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author iyasz
 */
public class Cashier extends javax.swing.JPanel {
    
    private Navigable navigator;
    private JPanel pnlMenuGrid;    // grid menu di kiri
    private JPanel pnlCartItems; 
    
    // Keranjang: key = product id, value = CartItem
    private Map<Integer, CartItem> cart = new LinkedHashMap<>();
    
    /**
     * Creates new form Cashier
     */
    public Cashier() {
        initComponents();
        initPanels();
    }
    
    public Cashier(Navigable navigator) {
        initComponents();
        this.navigator = navigator;
        initPanels();
        
        // ← Hubungkan tombol ke method
        btnBayar.addActionListener(e -> processBayar());
        btnClearCart.addActionListener(e -> clearCart());
    }
    
    private void initPanels() {
        pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));

        pnlMenuGrid = new JPanel(new WrapLayout(WrapLayout.LEFT, 12, 12));
        pnlMenuGrid.setBackground(new Color(245, 245, 245));
        scrollMenu.setViewportView(pnlMenuGrid);

        pnlCartItems = new JPanel();
        pnlCartItems.setLayout(new BoxLayout(pnlCartItems, BoxLayout.Y_AXIS));
        pnlCartItems.setBackground(Color.WHITE);
        scrollCart.setViewportView(pnlCartItems);

        // ← Tunggu sampai scrollMenu punya ukuran nyata
        scrollMenu.addComponentListener(new java.awt.event.ComponentAdapter() {
            private boolean loaded = false;

            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (!loaded && scrollMenu.getWidth() > 0) {
                    loaded = true;
                    loadMenuGrid(null);
                    loadCategoryFilter();
                }
            }
        });
    }
    
    private void loadMenuGrid(Integer categoryId) {
        pnlMenuGrid.removeAll();

        List<Product> products = categoryId == null
            ? ProductRepository.getAvailable()
            : ProductRepository.getAvailableByCategory(categoryId);
        

        for (Product product : products) {
            pnlMenuGrid.add(createMenuCard(product));
        }

        pnlMenuGrid.revalidate();
        pnlMenuGrid.repaint();
    }
    
    private JPanel createMenuCard(Product product) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setPreferredSize(new Dimension(150, 130));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblName = new JLabel(product.getName(), JLabel.CENTER);
        lblName.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel lblPrice = new JLabel("Rp " + String.format("%,d", product.getPrice()), JLabel.CENTER);
        lblPrice.setFont(new Font("Arial", Font.PLAIN, 11));
        lblPrice.setForeground(new Color(102, 102, 255));

        card.add(lblName,  BorderLayout.CENTER);
        card.add(lblPrice, BorderLayout.SOUTH);

        // Klik kartu → tambah ke keranjang
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                addToCart(product);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(240, 240, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }
    
    // Muat tombol filter kategori
    private void loadCategoryFilter() {
        pnlFilter.removeAll();
        pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8)); // ← tambah ini

        JButton btnAll = createFilterButton("Semua", null);
        btnAll.setBackground(Color.WHITE);
        pnlFilter.add(btnAll);
        

        List<Category> categories = CategoryRepository.getAll();
        for (Category cat : categories) {
            JButton newBtn = createFilterButton(cat.getName(), cat.getId());
            newBtn.setBackground(Color.WHITE);
            pnlFilter.add(newBtn);
        }

        pnlFilter.revalidate();
        pnlFilter.repaint();
    }
    
    private JButton createFilterButton(String label, Integer categoryId) {
        JButton btn = new JButton(label);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> loadMenuGrid(categoryId));
        return btn;
    }
    
    // Tambah produk ke keranjang
    private void addToCart(Product product) {
        if (cart.containsKey(product.getId())) {
            cart.get(product.getId()).incrementQuantity(); // sudah ada → tambah qty
        } else {
            cart.put(product.getId(), new CartItem(product)); // baru → tambah item
        }
        refreshCart();
    }
    
    // Refresh tampilan keranjang & total
    private void refreshCart() {
        pnlCartItems.removeAll();

        for (CartItem item : cart.values()) {
            if (item.getQuantity() > 0) {
                pnlCartItems.add(new CartItemRow(item, this::refreshTotal));
                pnlCartItems.add(Box.createVerticalStrut(4)); // jarak antar item
            }
        }

        pnlCartItems.revalidate();
        pnlCartItems.repaint();
        refreshTotal();
    }
    
    // Update label total
    private void refreshTotal() {
        long total = cart.values().stream()
            .filter(item -> item.getQuantity() > 0)
            .mapToLong(CartItem::getSubtotal)
            .sum();

        lblTotal.setText("Rp " + String.format("%,d", total)); // lblTotal = JLabel di designer
    }
    
    private void clearCart() {
        cart.clear();
        filed_cashier_name.setText("");
        filed_cashier_note.setText("");
        refreshCart(); // tampilan keranjang ikut kosong
    }
    
    private void processBayar() {
        // 1. Validasi keranjang tidak kosong
        boolean hasItem = cart.values().stream().anyMatch(i -> i.getQuantity() > 0);
        if (!hasItem) {
            JOptionPane.showMessageDialog(this,
                "Keranjang masih kosong!",
                "Validasi",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 2. Ambil data dari form
        String customerName = filed_cashier_name.getText().trim();
        String notes        = filed_cashier_note.getText().trim();

        // 3. Konfirmasi sebelum bayar
        long total = cart.values().stream()
            .filter(i -> i.getQuantity() > 0)
            .mapToLong(CartItem::getSubtotal)
            .sum();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Total: Rp " + String.format("%,d", total) + "\nProses pembayaran?",
            "Konfirmasi Bayar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        // 4. Simpan ke database
        String invoiceNumber = OrderRepository.save(customerName, notes, cart);

        // 5. Tampilkan hasil
        if (invoiceNumber != null) {
            JOptionPane.showMessageDialog(this,
                "Pembayaran berhasil!\nNo. Invoice: " + invoiceNumber,
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );
            clearCart(); // kosongkan keranjang setelah bayar
            
            // ← Refresh dashboard agar data terupdate
            if (navigator != null) {
                navigator.refreshPage("DASHBOARD"); // ← lewat navigator
            }
            
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal memproses pembayaran. Coba lagi.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlFilter = new javax.swing.JPanel();
        scrollMenu = new javax.swing.JScrollPane();
        scrollCart = new javax.swing.JScrollPane();
        lblTotal = new javax.swing.JLabel();
        btnBayar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnClearCart = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        filed_cashier_name = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        filed_cashier_note = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("Manajemen Kasir");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(98, 98, 98));
        jLabel2.setText("Proses pembayaran dan transaksi di kasir");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, -1, -1));

        pnlFilter.setBackground(new java.awt.Color(246, 246, 246));
        add(pnlFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 530, 100));
        add(scrollMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 530, 410));
        add(scrollCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 140, 330, 210));

        lblTotal.setText("0");
        add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 590, -1, -1));

        btnBayar.setBackground(new java.awt.Color(102, 102, 255));
        btnBayar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setText("Bayar");
        btnBayar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(btnBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 630, 160, 40));

        jLabel3.setText("Total :");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 590, -1, -1));

        btnClearCart.setBackground(new java.awt.Color(254, 254, 254));
        btnClearCart.setText("Clear");
        btnClearCart.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearCart.setPreferredSize(new java.awt.Dimension(72, 21));
        add(btnClearCart, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 630, 160, 40));

        jLabel4.setText("Nama");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 380, -1, -1));
        add(filed_cashier_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 400, 330, 40));

        jLabel5.setText("Note");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 460, -1, -1));

        filed_cashier_note.setColumns(20);
        filed_cashier_note.setRows(5);
        jScrollPane1.setViewportView(filed_cashier_note);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 480, 330, -1));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnClearCart;
    private javax.swing.JTextField filed_cashier_name;
    private javax.swing.JTextArea filed_cashier_note;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JScrollPane scrollCart;
    private javax.swing.JScrollPane scrollMenu;
    // End of variables declaration//GEN-END:variables
}
