/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.components;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author iyasz
 */
public class SummaryCard extends JPanel{
    
    
    private JLabel lblValue;
    
    public SummaryCard(String title, String value, Color accentColor) {
        setLayout(new BorderLayout(0, 8));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Garis aksen di atas kartu
        JPanel accent = new JPanel();
        accent.setBackground(accentColor);
        accent.setPreferredSize(new Dimension(0, 4));
        add(accent, BorderLayout.NORTH);

        // Judul
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(120, 120, 120));

        // Nilai
        lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblValue.setForeground(new Color(33, 33, 33));

        JPanel pnlText = new JPanel();
        pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
        pnlText.setBackground(Color.WHITE);
        pnlText.add(lblTitle);
        pnlText.add(Box.createVerticalStrut(8));
        pnlText.add(lblValue);

        add(pnlText, BorderLayout.CENTER);
    }

    // Update nilai kartu dari luar
    public void setValue(String value) {
        lblValue.setText(value);
    }
    
}
