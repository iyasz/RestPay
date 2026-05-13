/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.restpay.models;

/**
 *
 * @author iyasz
 */
public class Order {
    private String invoiceNumber;
    private String customerName;
    private long total;
    private String paidAt;

    public Order(String invoiceNumber, String customerName, long total, String paidAt) {
        this.invoiceNumber = invoiceNumber;
        this.customerName  = customerName;
        this.total         = total;
        this.paidAt        = paidAt;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public String getCustomerName()  { return customerName != null ? customerName : "-"; }
    public long getTotal()           { return total; }
    public String getPaidAt()        { return paidAt != null ? paidAt : "-"; }
}
