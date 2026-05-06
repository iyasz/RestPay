/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.models;

/**
 *
 * @author iyasz
 */
public class Category {
    private int id;
    private String name;
    
    public Category(int id, String name) {
        this.id   = id;
        this.name = name;
    }
    
    public int getId()      { return id; }
    public String getName() { return name; }
    
    @Override
    public String toString() {
        return name;
    }
    
}
