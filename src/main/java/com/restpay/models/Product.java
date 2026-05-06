/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.models;

/**
 *
 * @author iyasz
 */
public class Product {
    private int id;
    private int categoryId;
    private String name;
    private String description;
    private long price;
    private boolean isAvailable;
    
        public Product(int categoryId, String name, String description, long price, boolean isAvailable) {
        this.categoryId  = categoryId;
        this.name        = name;
        this.description = description;
        this.price       = price;
        this.isAvailable = isAvailable;
    }
    
    // Getters
    public int getCategoryId()    { return categoryId; }
    public String getName()       { return name; }
    public String getDescription(){ return description; }
    public long getPrice()      { return price; }
    public boolean isAvailable()  { return isAvailable; }
    
}
