package com.store.database;

import com.store.model.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class StoreDatabase {
    private static final String PRODUCTS_FILE = "products.dat";
    private static final String CUSTOMERS_FILE = "customers.dat";
    private static final String SALES_FILE = "sales.dat";
    
    private Map<String, Product> products;
    private Map<String, Customer> customers;
    private Map<String, Sale> sales;
    
    private static StoreDatabase instance;
    
    private StoreDatabase() {
        products = new HashMap<>();
        customers = new HashMap<>();
        sales = new HashMap<>();
        loadData();
        initializeSampleData();
    }
    
    public static synchronized StoreDatabase getInstance() {
        if (instance == null) {
            instance = new StoreDatabase();
        }
        return instance;
    }
    
    // Product CRUD Operations
    public void addProduct(Product product) throws IllegalArgumentException {
        if (!product.validate()) {
            throw new IllegalArgumentException("Invalid product data");
        }
        products.put(product.getId(), product);
        saveProducts();
    }
    
    public void updateProduct(Product product) throws IllegalArgumentException {
        if (!product.validate()) {
            throw new IllegalArgumentException("Invalid product data");
        }
        if (!products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product not found");
        }
        products.put(product.getId(), product);
        saveProducts();
    }
    
    public void deleteProduct(String productId) throws IllegalArgumentException {
        if (!products.containsKey(productId)) {
            throw new IllegalArgumentException("Product not found");
        }
        products.remove(productId);
        saveProducts();
    }
    
    public Product getProduct(String productId) {
        return products.get(productId);
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    
    public List<Product> getLowStockProducts(int threshold) {
        return products.values().stream()
            .filter(p -> p.getQuantity() <= threshold)
            .collect(Collectors.toList());
    }
    
    // Customer CRUD Operations
    public void addCustomer(Customer customer) throws IllegalArgumentException {
        if (!customer.validate()) {
            throw new IllegalArgumentException("Invalid customer data");
        }
        customers.put(customer.getId(), customer);
        saveCustomers();
    }
    
    public void updateCustomer(Customer customer) throws IllegalArgumentException {
        if (!customer.validate()) {
            throw new IllegalArgumentException("Invalid customer data");
        }
        if (!customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Customer not found");
        }
        customers.put(customer.getId(), customer);
        saveCustomers();
    }
    
    public void deleteCustomer(String customerId) throws IllegalArgumentException {
        if (!customers.containsKey(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }
        customers.remove(customerId);
        saveCustomers();
    }
    
    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    // Sale Operations
    public void addSale(Sale sale) throws IllegalArgumentException {
        if (!sale.validate()) {
            throw new IllegalArgumentException("Invalid sale data");
        }
        
        // Update product quantities
        for (Map.Entry<String, Integer> entry : sale.getProducts().entrySet()) {
            Product product = products.get(entry.getKey());
            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + entry.getKey());
            }
            if (product.getQuantity() < entry.getValue()) {
                throw new IllegalArgumentException("Insufficient stock for: " + product.getName());
            }
            product.setQuantity(product.getQuantity() - entry.getValue());
        }
        
        sales.put(sale.getSaleId(), sale);
        saveAll();
    }
    
    public List<Sale> getAllSales() {
        return new ArrayList<>(sales.values());
    }
    
    public double getTotalRevenue() {
        return sales.values().stream()
            .mapToDouble(Sale::getTotalAmount)
            .sum();
    }
    
    // File I/O Operations
    private void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCTS_FILE))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.err.println("Error saving products: " + e.getMessage());
        }
    }
    
    private void saveCustomers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }
    
    private void saveSales() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SALES_FILE))) {
            oos.writeObject(sales);
        } catch (IOException e) {
            System.err.println("Error saving sales: " + e.getMessage());
        }
    }
    
    private void saveAll() {
        saveProducts();
        saveCustomers();
        saveSales();
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCTS_FILE))) {
            products = (Map<String, Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            products = new HashMap<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CUSTOMERS_FILE))) {
            customers = (Map<String, Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            customers = new HashMap<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SALES_FILE))) {
            sales = (Map<String, Sale>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            sales = new HashMap<>();
        }
    }
    
    private void initializeSampleData() {
        if (products.isEmpty()) {
            try {
                addProduct(new Product("P001", "Laptop", "Electronics", 999.99, 10, "High-performance laptop"));
                addProduct(new Product("P002", "Mouse", "Electronics", 25.50, 50, "Wireless computer mouse"));
                addProduct(new Product("P003", "Notebook", "Stationery", 5.99, 100, "A4 size notebook"));
                addProduct(new Product("P004", "Pen", "Stationery", 1.99, 200, "Blue ink ballpoint pen"));
                addProduct(new Product("P005", "Coffee Mug", "Kitchen", 12.99, 30, "Ceramic coffee mug"));
            } catch (IllegalArgumentException e) {
                System.err.println("Error initializing sample products: " + e.getMessage());
            }
        }
        
        if (customers.isEmpty()) {
            try {
                addCustomer(new Customer("C001", "Alice Johnson", "alice@email.com", "1234567890", "123 Main St"));
                addCustomer(new Customer("C002", "Bob Smith", "bob@email.com", "9876543210", "456 Oak Ave"));
                addCustomer(new Customer("C003", "Carol Davis", "carol@email.com", "5551234567", "789 Pine Rd"));
            } catch (IllegalArgumentException e) {
                System.err.println("Error initializing sample customers: " + e.getMessage());
            }
        }
    }
}