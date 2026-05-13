/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.models;

/**
 *
 * @author iyasz
 */
public class CartItem {
    private Product product;
    private int quantity;
    
    public CartItem(Product product) {
        this.product  = product;
        this.quantity = 1; // default 1 saat pertama ditambah
    }
    
    public Product getProduct()  { return product; }
    public int getQuantity()     { return quantity; }

    public void incrementQuantity() { quantity++; }
    public void decrementQuantity() { if (quantity > 0) quantity--; }

    public long getSubtotal() {
        return product.getPrice() * quantity;
    }
    
}
