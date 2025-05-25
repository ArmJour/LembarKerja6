package Kasus2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIManager {
    private JFrame frame;
    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField productPriceField;
    private JTextField quantityField;
    private JTextArea outputArea;
    private Inventory inventory;
    private int[] productIds = new int[100]; // Array to store product IDs

    public GUIManager(Inventory inventory) {
        this.inventory = inventory;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Inventory Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 550); // Width, Height   
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setResizable(false);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5)); // 4 rows, 2 cols, with gaps
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

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

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String productId = productIdField.getText();
                    String productName = productNameField.getText();
                    if (productId.isEmpty() || productName.isEmpty() || productPriceField.getText().isEmpty() || quantityField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please fill in all product details and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double productPrice = Double.parseDouble(productPriceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    Product product = new Product(productId, productName, productPrice);
                    inventory.addProduct(product, quantity);
                    outputArea.setText("Added " + quantity + " of " + product.getName() + " (ID: " + product.getProductId() + "). Current stock: " + inventory.checkStock(product));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for price and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton removeButton = new JButton("Remove Product");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String productId = productIdField.getText();
                    String productName = productNameField.getText();
                    if (productId.isEmpty() || productName.isEmpty() || quantityField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please fill in all product details and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int quantity = Integer.parseInt(quantityField.getText());
                    Product product = new Product(productId, productName, 0); // Price is not needed for removal
                    inventory.removeProduct(product, quantity);
                    outputArea.setText("Removed " + quantity + " of " + product.getName() + " (ID: " + product.getProductId() + "). Current stock: " + inventory.checkStock(product));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid numeric value for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton checkStockButton = new JButton("Check Stock");
        checkStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String productId = productIdField.getText();
                    if (productId.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please enter a Product ID to check stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Product product = new Product(productId, "", 0); // Name and price are not needed for stock check
                    int stock = inventory.checkStock(product);
                    if (stock == 0) {
                        outputArea.setText("No stock available for Product ID " + product.getProductId() + ".");
                    } else if (stock < 0) {
                        outputArea.setText("Negative stock for Product ID " + product.getProductId() + ". Please check the inventory.");
                    }
                    else
                    outputArea.setText("Current stock for Product ID " + product.getProductId() + ": " + stock);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(checkStockButton);

        // --- Combine input and button panels into a central panel ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Stack vertically
        centerPanel.add(inputPanel);
        centerPanel.add(Box.createVerticalStrut(20)); // Add some space
        centerPanel.add(buttonPanel);
        // Align the content of centerPanel to its X-axis center
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Important for BoxLayout

        // Wrap the centerPanel in another panel with FlowLayout for true centering in BorderLayout.CENTER
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.add(centerPanel);

        // --- Output Area ---
        outputArea = new JTextArea(10, 50); // Set initial rows and columns for JTextArea
        outputArea.setEditable(false);
        outputArea.setLineWrap(true); // Wrap lines
        outputArea.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to the frame using BorderLayout regions
        frame.add(wrapperPanel, BorderLayout.CENTER); // Main content in the center
        frame.add(scrollPane, BorderLayout.SOUTH); // Output at the bottom

        frame.setVisible(true);
    }
}
