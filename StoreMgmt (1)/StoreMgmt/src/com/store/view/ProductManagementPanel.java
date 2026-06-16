package com.store.view;

import com.store.controller.ProductController;
import com.store.model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductManagementPanel extends JPanel {
    private ProductController controller;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtCategory, txtPrice, txtQuantity, txtDescription;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    
    public ProductManagementPanel() {
        controller = new ProductController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadProducts();
    }
    
    private void initComponents() {
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtId = new JTextField(15);
        txtName = new JTextField(15);
        txtCategory = new JTextField(15);
        txtPrice = new JTextField(15);
        txtQuantity = new JTextField(15);
        txtDescription = new JTextField(15);
        
        addFormField(formPanel, gbc, "Product ID:", txtId, 0);
        addFormField(formPanel, gbc, "Name:", txtName, 1);
        addFormField(formPanel, gbc, "Category:", txtCategory, 2);
        addFormField(formPanel, gbc, "Price:", txtPrice, 3);
        addFormField(formPanel, gbc, "Quantity:", txtQuantity, 4);
        addFormField(formPanel, gbc, "Description:", txtDescription, 5);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAdd = new JButton("Add Product");
        btnUpdate = new JButton("Update Product");
        btnDelete = new JButton("Delete Product");
        btnClear = new JButton("Clear");
        
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Table Panel
        String[] columns = {"ID", "Name", "Category", "Price", "Quantity", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedProduct();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Products List"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, 
                             String label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }
    
    private void addProduct() {
        try {
            Product product = new Product(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtCategory.getText().trim(),
                Double.parseDouble(txtPrice.getText().trim()),
                Integer.parseInt(txtQuantity.getText().trim()),
                txtDescription.getText().trim()
            );
            
            controller.addProduct(product);
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearForm();
            loadProducts();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProduct() {
        try {
            Product product = new Product(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtCategory.getText().trim(),
                Double.parseDouble(txtPrice.getText().trim()),
                Integer.parseInt(txtQuantity.getText().trim()),
                txtDescription.getText().trim()
            );
            
            controller.updateProduct(product);
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            clearForm();
            loadProducts();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this product?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String productId = (String) tableModel.getValueAt(row, 0);
                controller.deleteProduct(productId);
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                clearForm();
                loadProducts();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadSelectedProduct() {
        int row = productTable.getSelectedRow();
        if (row != -1) {
            txtId.setText((String) tableModel.getValueAt(row, 0));
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtCategory.setText((String) tableModel.getValueAt(row, 2));
            txtPrice.setText(tableModel.getValueAt(row, 3).toString());
            txtQuantity.setText(tableModel.getValueAt(row, 4).toString());
            txtDescription.setText((String) tableModel.getValueAt(row, 5));
        }
    }
    
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        txtDescription.setText("");
        productTable.clearSelection();
    }
    
    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = controller.getAllProducts();
        for (Product product : products) {
            tableModel.addRow(new Object[] {
                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("$%.2f", product.getPrice()),
                product.getQuantity(),
                product.getDescription()
            });
        }
    }
}