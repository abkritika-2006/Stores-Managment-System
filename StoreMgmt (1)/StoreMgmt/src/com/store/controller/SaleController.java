package com.store.controller;

import com.store.database.StoreDatabase;
import com.store.model.Sale;
import java.util.List;

public class SaleController {
    private StoreDatabase database;
    
    public SaleController() {
        database = StoreDatabase.getInstance();
    }
    
    public void addSale(Sale sale) {
        database.addSale(sale);
    }
    
    public List<Sale> getAllSales() {
        return database.getAllSales();
    }
    
    public double getTotalRevenue() {
        return database.getTotalRevenue();
    }
}