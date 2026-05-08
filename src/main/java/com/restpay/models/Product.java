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
    
    private String categoryName;
    
    public Product(int categoryId, String name, String description, long price, boolean isAvailable) {
        this.categoryId  = categoryId;
        this.name        = name;
        this.description = description;
        this.price       = price;
        this.isAvailable = isAvailable;
    }
    
    public Product(int id, int categoryId, String categoryName, String name,
                   String description, long price, boolean isAvailable) {
        this.id           = id;
        this.categoryId   = categoryId;
        this.categoryName = categoryName;
        this.name         = name;
        this.description  = description;
        this.price        = price;
        this.isAvailable  = isAvailable;
    }
    
    // Getters
    public int getId()       { return id; }
    public int getCategoryId()    { return categoryId; }
    public String getName()       { return name; }
    public String getDescription(){ return description; }
    public long getPrice()      { return price; }
    public boolean isAvailable()  { return isAvailable; }
    public String getCategoryName() { return categoryName; }
    
}
