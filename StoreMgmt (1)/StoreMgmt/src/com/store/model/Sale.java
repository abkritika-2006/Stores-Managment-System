package com.store.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String saleId;
    private String customerId;
    private LocalDate saleDate;
    private Map<String, Integer> products; // productId -> quantity
    private double totalAmount;
    private String paymentMethod;
    
    public Sale() {
        this.saleDate = LocalDate.now();
        this.products = new HashMap<>();
        this.totalAmount = 0.0;
    }
    
    public Sale(String saleId, String customerId) {
        this.saleId = saleId;
        this.customerId = customerId;
        this.saleDate = LocalDate.now();
        this.products = new HashMap<>();
        this.totalAmount = 0.0;
    }
    
    // Getters and Setters
    public String getSaleId() { return saleId; }
    public void setSaleId(String saleId) { this.saleId = saleId; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    
    public Map<String, Integer> getProducts() { return products; }
    public void setProducts(Map<String, Integer> products) { this.products = products; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public void addProduct(String productId, int quantity) {
        products.put(productId, products.getOrDefault(productId, 0) + quantity);
    }
    
    public void calculateTotal(Map<String, Product> productMap) {
        totalAmount = 0.0;
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            Product product = productMap.get(entry.getKey());
            if (product != null) {
                totalAmount += product.getPrice() * entry.getValue();
            }
        }
    }
    
    public boolean validate() {
        return saleId != null && !saleId.trim().isEmpty() &&
               customerId != null && !customerId.trim().isEmpty() &&
               !products.isEmpty() &&
               totalAmount > 0 &&
               paymentMethod != null && !paymentMethod.trim().isEmpty();
    }
}