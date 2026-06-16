package com.store.view;

import com.store.controller.CustomerController;
import com.store.model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerManagementPanel extends JPanel {
    private CustomerController controller;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtEmail, txtPhone, txtAddress;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    
    public CustomerManagementPanel() {
        controller = new CustomerController();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadCustomers();
    }
    
    private void initComponents() {
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtId = new JTextField(20);
        txtName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPhone = new JTextField(20);
        txtAddress = new JTextField(20);
        
        addFormField(formPanel, gbc, "Customer ID:", txtId, 0);
        addFormField(formPanel, gbc, "Name:", txtName, 1);
        addFormField(formPanel, gbc, "Email:", txtEmail, 2);
        addFormField(formPanel, gbc, "Phone:", txtPhone, 3);
        addFormField(formPanel, gbc, "Address:", txtAddress, 4);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAdd = new JButton("Add Customer");
        btnUpdate = new JButton("Update Customer");
        btnDelete = new JButton("Delete Customer");
        btnClear = new JButton("Clear");
        
        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Table Panel
        String[] columns = {"ID", "Name", "Email", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCustomer();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Customers List"));
        
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
    
    private void addCustomer() {
        try {
            Customer customer = new Customer(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                txtAddress.getText().trim()
            );
            
            controller.addCustomer(customer);
            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            clearForm();
            loadCustomers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCustomer() {
        try {
            Customer customer = new Customer(
                txtId.getText().trim(),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                txtAddress.getText().trim()
            );
            
            controller.updateCustomer(customer);
            JOptionPane.showMessageDialog(this, "Customer updated successfully!");
            clearForm();
            loadCustomers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this customer?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String customerId = (String) tableModel.getValueAt(row, 0);
                controller.deleteCustomer(customerId);
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
                clearForm();
                loadCustomers();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadSelectedCustomer() {
        int row = customerTable.getSelectedRow();
        if (row != -1) {
            txtId.setText((String) tableModel.getValueAt(row, 0));
            txtName.setText((String) tableModel.getValueAt(row, 1));
            txtEmail.setText((String) tableModel.getValueAt(row, 2));
            txtPhone.setText((String) tableModel.getValueAt(row, 3));
            txtAddress.setText((String) tableModel.getValueAt(row, 4));
        }
    }
    
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        customerTable.clearSelection();
    }
    
    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = controller.getAllCustomers();
        for (Customer customer : customers) {
            tableModel.addRow(new Object[] {
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress()
            });
        }
    }
}