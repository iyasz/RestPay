/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.restpay;

import com.restpay.models.Product;

/**
 *
 * @author iyasz
 */
public interface Navigable {
    void navigateTo(String pageName);
    void navigateAndRefresh(String pageName);
    void navigateToEdit(Product product);
}
