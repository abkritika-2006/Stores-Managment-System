package com.store.view;

import com.store.controller.SaleController;
import com.store.controller.ProductController;
import com.store.controller.CustomerController;
import com.store.model.Sale;
import com.store.model.Product;
import com.store.model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesPanel extends JPanel {
    private SaleController saleController;
    private ProductController productController;
    private CustomerController customerController;
    
    private JTable salesTable;
    private DefaultTableModel salesTableModel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    
    private JComboBox<Product> productComboBox;
    private JComboBox<Customer> customerComboBox;
    private JComboBox<String> paymentMethodComboBox;
    private JTextField txtQuantity, txtSaleId;
    private JButton btnAddToCart, btnRemoveFromCart, btnProcessSale, btnClearCart;
    private JLabel lblTotalAmount;
    
    private Map<String, Integer> cartItems;
    private double cartTotal;
    
    public SalesPanel() {
        saleController = new SaleController();
        productController = new ProductController();
        customerController = new CustomerController();
        
        cartItems = new HashMap<>();
        cartTotal = 0.0;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadSales();
    }
    
    private void initComponents() {
        // Sales Form Panel
        JPanel salesFormPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        salesFormPanel.setBorder(BorderFactory.createTitledBorder("New Sale"));
        
        txtSaleId = new JTextField();
        customerComboBox = new JComboBox<>();
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card", "Digital Wallet"});
        
        salesFormPanel.add(new JLabel("Sale ID:"));
        salesFormPanel.add(txtSaleId);
        salesFormPanel.add(new JLabel("Customer:"));
        salesFormPanel.add(customerComboBox);
        salesFormPanel.add(new JLabel("Payment Method:"));
        salesFormPanel.add(paymentMethodComboBox);
        
        // Cart Management Panel
        JPanel cartPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Add to Cart"));
        
        productComboBox = new JComboBox<>();
        txtQuantity = new JTextField("1");
        
        cartPanel.add(new JLabel("Product:"));
        cartPanel.add(productComboBox);
        cartPanel.add(new JLabel("Quantity:"));
        cartPanel.add(txtQuantity);
        
        btnAddToCart = new JButton("Add to Cart");
        btnRemoveFromCart = new JButton("Remove Selected");
        
        cartPanel.add(btnAddToCart);
        cartPanel.add(btnRemoveFromCart);
        
        // Cart Table
        String[] cartColumns = {"Product ID", "Product Name", "Price", "Quantity", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        
        // Total and Action Panel
        JPanel totalPanel = new JPanel(new FlowLayout());
        lblTotalAmount = new JLabel("Total: $0.00");
        lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 16));
        
        btnProcessSale = new JButton("Process Sale");
        btnClearCart = new JButton("Clear Cart");
        
        totalPanel.add(lblTotalAmount);
        totalPanel.add(btnProcessSale);
        totalPanel.add(btnClearCart);
        
        // Sales History Table
        String[] salesColumns = {"Sale ID", "Customer", "Date", "Total Amount", "Payment Method"};
        salesTableModel = new DefaultTableModel(salesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        salesTable = new JTable(salesTableModel);
        JScrollPane salesScrollPane = new JScrollPane(salesTable);
        salesScrollPane.setBorder(BorderFactory.createTitledBorder("Sales History"));
        
        // Layout Organization
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel formContainer = new JPanel(new GridLayout(2, 1, 5, 5));
        formContainer.add(salesFormPanel);
        formContainer.add(cartPanel);
        
        topPanel.add(formContainer, BorderLayout.CENTER);
        topPanel.add(totalPanel, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.add(cartScrollPane);
        centerPanel.add(salesScrollPane);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Event Listeners
        setupEventListeners();
        updateComboBoxes();
        updateTotal();
    }
    
    private void setupEventListeners() {
        btnAddToCart.addActionListener(e -> addToCart());
        btnRemoveFromCart.addActionListener(e -> removeFromCart());
        btnProcessSale.addActionListener(e -> processSale());
        btnClearCart.addActionListener(e -> clearCart());
    }
    
    private void updateComboBoxes() {
        productComboBox.removeAllItems();
        customerComboBox.removeAllItems();
        
        for (Product product : productController.getAllProducts()) {
            if (product.getQuantity() > 0) {
                productComboBox.addItem(product);
            }
        }
        
        for (Customer customer : customerController.getAllCustomers()) {
            customerComboBox.addItem(customer);
        }
    }
    
    private void addToCart() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product!");
            return;
        }
        
        try {
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive!");
                return;
            }
            
            if (quantity > selectedProduct.getQuantity()) {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient stock! Available: " + selectedProduct.getQuantity());
                return;
            }
            
            // Add to cart
            cartItems.put(selectedProduct.getId(), 
                         cartItems.getOrDefault(selectedProduct.getId(), 0) + quantity);
            
            updateCartTable();
            updateTotal();
            txtQuantity.setText("1");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove!");
            return;
        }
        
        String productId = (String) cartTableModel.getValueAt(row, 0);
        cartItems.remove(productId);
        updateCartTable();
        updateTotal();
    }
    
    private void processSale() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        
        if (txtSaleId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Sale ID!");
            return;
        }
        
        if (customerComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer!");
            return;
        }
        
        if (paymentMethodComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a payment method!");
            return;
        }
        
        try {
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            Sale sale = new Sale(txtSaleId.getText().trim(), selectedCustomer.getId());
            
            // Add all cart items to sale
            for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
                sale.addProduct(entry.getKey(), entry.getValue());
            }
            
            // Calculate total
            sale.calculateTotal(productController.getAllProducts().stream()
                .collect(java.util.stream.Collectors.toMap(Product::getId, p -> p)));
            
            sale.setPaymentMethod(paymentMethodComboBox.getSelectedItem().toString());
            
            // Process sale
            saleController.addSale(sale);
            
            JOptionPane.showMessageDialog(this, 
                "Sale processed successfully!\nTotal: $" + String.format("%.2f", sale.getTotalAmount()));
            
            clearCart();
            updateComboBoxes();
            loadSales();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearCart() {
        cartItems.clear();
        updateCartTable();
        updateTotal();
        txtSaleId.setText("");
    }
    
    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        cartTotal = 0.0;
        
        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            Product product = productController.getProduct(entry.getKey());
            if (product != null) {
                double subtotal = product.getPrice() * entry.getValue();
                cartTotal += subtotal;
                
                cartTableModel.addRow(new Object[] {
                    product.getId(),
                    product.getName(),
                    String.format("$%.2f", product.getPrice()),
                    entry.getValue(),
                    String.format("$%.2f", subtotal)
                });
            }
        }
    }
    
    private void updateTotal() {
        lblTotalAmount.setText(String.format("Total: $%.2f", cartTotal));
    }
    
    private void loadSales() {
        salesTableModel.setRowCount(0);
        List<Sale> sales = saleController.getAllSales();
        
        for (Sale sale : sales) {
            Customer customer = customerController.getCustomer(sale.getCustomerId());
            String customerName = customer != null ? customer.getName() : "Unknown";
            
            salesTableModel.addRow(new Object[] {
                sale.getSaleId(),
                customerName,
                sale.getSaleDate(),
                String.format("$%.2f", sale.getTotalAmount()),
                sale.getPaymentMethod()
            });
        }
    }
}