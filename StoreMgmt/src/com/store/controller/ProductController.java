package com.store.controller;

import com.store.database.StoreDatabase;
import com.store.model.Product;
import java.util.List;

public class ProductController {
    private StoreDatabase database;
    
    public ProductController() {
        database = StoreDatabase.getInstance();
    }
    
    public void addProduct(Product product) {
        database.addProduct(product);
    }
    
    public void updateProduct(Product product) {
        database.updateProduct(product);
    }
    
    public void deleteProduct(String productId) {
        database.deleteProduct(productId);
    }
    
    public Product getProduct(String productId) {
        return database.getProduct(productId);
    }
    
    public List<Product> getAllProducts() {
        return database.getAllProducts();
    }
    
    public List<Product> getLowStockProducts(int threshold) {
        return database.getLowStockProducts(threshold);
    }
}