package com.store.controller;

import com.store.database.StoreDatabase;
import com.store.model.Customer;
import java.util.List;

public class CustomerController {
    private StoreDatabase database;
    
    public CustomerController() {
        database = StoreDatabase.getInstance();
    }
    
    public void addCustomer(Customer customer) {
        database.addCustomer(customer);
    }
    
    public void updateCustomer(Customer customer) {
        database.updateCustomer(customer);
    }
    
    public void deleteCustomer(String customerId) {
        database.deleteCustomer(customerId);
    }
    
    public Customer getCustomer(String customerId) {
        return database.getCustomer(customerId);
    }
    
    public List<Customer> getAllCustomers() {
        return database.getAllCustomers();
    }
}