package Kasus2;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class GUIManager {
    private JFrame frame;
    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField productPriceField;
    private JTextField quantityField;
    private JTextArea outputArea;
    private StockManager stockManager; // Using your StockManager

    public GUIManager(StockManager stockManager) {
        this.stockManager = stockManager;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Inventory and Stock Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // --- Input Panel ---  
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), "Product Details"));

        inputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        inputPanel.add(productIdField);

        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        inputPanel.add(productNameField);

        inputPanel.add(new JLabel("Product Price:"));
        productPriceField = new JTextField();
        inputPanel.add(productPriceField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(5,5,5,5), "Actions"));

        JButton addUpdateStockButton = new JButton("Add/Update Stock");
        addUpdateStockButton.setToolTipText("Add a new product or increase stock for an existing one.");
        addUpdateStockButton.addActionListener(this::handleAddUpdateStock);
        buttonPanel.add(addUpdateStockButton);

        JButton sellRemoveStockButton = new JButton("Sell/Remove Stock");
        sellRemoveStockButton.setToolTipText("Decrease stock for an existing product.");
        sellRemoveStockButton.addActionListener(this::handleSellRemoveStock);
        buttonPanel.add(sellRemoveStockButton);

        JButton checkStockButton = new JButton("Check Stock");
        checkStockButton.setToolTipText("Check current stock for a product.");
        checkStockButton.addActionListener(this::handleCheckStock);
        buttonPanel.add(checkStockButton);

        JButton placeOrderButton = new JButton("Place Customer Order");
        placeOrderButton.setToolTipText("Decrease stock via StockUpdater (simulates customer order).");
        placeOrderButton.addActionListener(this::handlePlaceCustomerOrder);
        buttonPanel.add(placeOrderButton);

        JButton processReturnButton = new JButton("Process Customer Return");
        processReturnButton.setToolTipText("Increase stock via StockUpdater (simulates customer return).");
        processReturnButton.addActionListener(this::handleProcessCustomerReturn);
        buttonPanel.add(processReturnButton);
        
        JButton clearFieldsButton = new JButton("Clear Fields");
        clearFieldsButton.addActionListener(e -> clearInputFields());
        buttonPanel.add(clearFieldsButton);


        JPanel topPanel = new JPanel(new BorderLayout(0,10));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // --- Output Area ---
        outputArea = new JTextArea(12, 50);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(5,5,5,5), "System Log"));
        
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }
    
    private void clearInputFields() {
        productIdField.setText("");
        productNameField.setText("");
        productPriceField.setText("");
        quantityField.setText("");
    }

    private void handleAddUpdateStock(ActionEvent e) {
        try {
            String productId = productIdField.getText().trim();
            String productName = productNameField.getText().trim();
            String priceText = productPriceField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (productId.isEmpty() || productName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all product details and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double productPrice = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(frame, "Quantity must be a positive number to add/update stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (productPrice < 0) {
                JOptionPane.showMessageDialog(frame, "Price cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = new Product(productId, productName, productPrice);
            /*
             * memakai syncStock dari class StockManager, yang memanggil inventory.updateStock
             * inventory.addProduct secara langsung memanggil updateStock.
             * untuk menambah products baru atau menambah stock, jumlah harus berupa positive.    
             */
            stockManager.syncStock(product, quantity); 
            
            outputArea.append("Stock updated for " + productName + " (ID: " + productId + ") by " + quantity + ".\n");
            outputArea.append("Current stock: " + stockManager.inventory.checkStock(product) + "\n"); 
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for price and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    private void handleSellRemoveStock(ActionEvent e) {
        try {
            String productId = productIdField.getText().trim();
            /*
             *  productName dan productPrice tidak diperlukan untuk mengurangi stock,
             *  karena kita hanya perlu ID untuk mengidentifikasi produk.
             */
            
            String quantityText = quantityField.getText().trim();

            if (productId.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in Product ID and Quantity to sell/remove.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(frame, "Quantity to sell/remove must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product product = new Product(productId, productNameField.getText().trim(), 0.0); 

            /*
             * Untuk mengurangi stock, kita menggunakan metode syncStock dari StockManager.
             * ini akan memanggil Inventory.updateStock dengan jumlah negatif.
             */
            stockManager.syncStock(product, -quantity); //
            
            outputArea.append("Stock decreased for Product ID " + productId + " by " + quantity + ".\n");
            outputArea.append("Current stock: " + stockManager.inventory.checkStock(product) + "\n"); //
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid numeric value for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    private void handleCheckStock(ActionEvent e) {
        try {
            String productId = productIdField.getText().trim();
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a Product ID to check stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product product = new Product(productId, "", 0.0); 
            int stock = stockManager.inventory.checkStock(product); //
            
            /*
             * untuk mengecek stock, kita menggunakan Inventory.checkStock.
             * ini akan mengembalikan jumlah stock untuk produk yang diberikan ID-nya.
             */
            outputArea.append("Current stock for Product ID " + product.getProductId() + ": " + stock + "\n");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    private void handlePlaceCustomerOrder(ActionEvent e) {
        try {
            String productId = productIdField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (productId.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in Product ID and Quantity for the order.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(frame, "Order quantity must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product product = new Product(productId, productNameField.getText().trim(), 0.0); 

            StockUpdater updater = new StockUpdater("order", product, quantity, stockManager); 
            updater.run();

            outputArea.append("Customer order processing initiated for " + quantity + " of Product ID " + productId + " (will decrease stock).\n");
            outputArea.append("Stock update is asynchronous. Use 'Check Stock' for latest.\n");
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid numeric value for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputArea.append("Error initiating order: " + ex.getMessage() + "\n");
        }
    }

    private void handleProcessCustomerReturn(ActionEvent e) {
        try {
            String productId = productIdField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (productId.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in Product ID and Quantity for the return.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int quantity = Integer.parseInt(quantityText);
             if (quantity <= 0) {
                JOptionPane.showMessageDialog(frame, "Return quantity must be positive.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product product = new Product(productId, productNameField.getText().trim(), 0.0);

            StockUpdater updater = new StockUpdater("return", product, quantity, stockManager); 
            updater.run();

            outputArea.append("Customer return processing initiated for " + quantity + " of Product ID " + productId + " (will increase stock).\n");
            outputArea.append("Stock update is asynchronous. Use 'Check Stock' for latest.\n");
            clearInputFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid numeric value for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            outputArea.append("Error initiating return: " + ex.getMessage() + "\n");
        }
    }
}