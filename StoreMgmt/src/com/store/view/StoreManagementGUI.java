package com.store.view;

import javax.swing.*;
import java.awt.*;

public class StoreManagementGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private ProductManagementPanel productPanel;
    private CustomerManagementPanel customerPanel;
    private SalesPanel salesPanel;
    
    public StoreManagementGUI() {
        setTitle("Store Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        initComponents();
        setVisible(true);
    }
    
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        productPanel = new ProductManagementPanel();
        customerPanel = new CustomerManagementPanel();
        salesPanel = new SalesPanel();
        
        tabbedPane.addTab("📦 Product Management", productPanel);
        tabbedPane.addTab("👥 Customer Management", customerPanel);
        tabbedPane.addTab("💰 Sales", salesPanel);
        
        add(tabbedPane);
    }
}