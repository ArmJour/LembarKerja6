package Kasus2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class GUIManager {
    private JFrame frame;
    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField productPriceField;
    private JTextField quantityField;
    private JTextArea outputArea;
    private StockManager stockManager;
    private ExecutorService simulationExecutor;

    private Map<String, Product> productsInGuiInventory = new ConcurrentHashMap<>();

    public GUIManager(StockManager stockManager) {
        this.stockManager = stockManager;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Inventory and Stock Management System");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (simulationExecutor != null && !simulationExecutor.isTerminated()) {
                    int choice = JOptionPane.showConfirmDialog(frame,
                            "A simulation might be running. Are you sure you want to exit?",
                            "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (choice == JOptionPane.NO_OPTION) {
                        return;
                    }
                    outputArea.append("Attempting to shut down simulation executor...\n");
                    simulationExecutor.shutdownNow();
                    try {
                        if (!simulationExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                            outputArea.append("Simulation executor did not terminate quickly.\n");
                        } else {
                             outputArea.append("Simulation executor shut down.\n");
                        }
                    } catch (InterruptedException ex) {
                        outputArea.append("Interrupted while waiting for executor shutdown.\n");
                        Thread.currentThread().interrupt();
                    }
                }
                frame.dispose();
                System.exit(0);
            }
        });

        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

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

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
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

        JButton startSimulationButton = new JButton("Start Buy/Sell Simulation");
        startSimulationButton.setToolTipText("Simulate random buy/sell operations on products added via GUI.");
        startSimulationButton.addActionListener(this::handleStartSimulation);
        buttonPanel.add(startSimulationButton);

        JPanel topPanel = new JPanel(new BorderLayout(0,10));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        outputArea = new JTextArea(15, 50);
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
            stockManager.syncStock(product, quantity); 
            
            productsInGuiInventory.put(product.getProductId(), product);
            
            SwingUtilities.invokeLater(() -> {
                outputArea.append("Stock updated for " + productName + " (ID: " + productId + ") by " + quantity + ".\n");
                outputArea.append("Current stock: " + stockManager.inventory.checkStock(product) + "\n"); //
                outputArea.append(productName + " (ID: " + productId + ") is now available for simulation.\n");
                clearInputFields();
            });
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for price and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            final String errorMessage = ex.getMessage();
            JOptionPane.showMessageDialog(frame, "An error occurred: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(() -> outputArea.append("Error: " + errorMessage + "\n"));
        }
    }

    private void handleSellRemoveStock(ActionEvent e) {
        try {
            String productId = productIdField.getText().trim();
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

            /*
             * Memerlukan detail produk untuk menampilkan nama, tetapi untuk stockManager.syncStock, ID adalah kunci.
             * jika produk ada di peta GUI kami, gunakan detailnya. Jika tidak, buat placeholder.
             */
            Product productFromMap = productsInGuiInventory.get(productId);
            Product productToProcess;
            String productNameForDisplay = productId;

            if (productFromMap != null) {
                productToProcess = productFromMap;
                productNameForDisplay = productFromMap.getName();
            } else {

                /*
                 * jika tidak ada di map, mungkin masih ada di inventaris jika ditambahkan sebelumnya,
                 * untuk syncStock, objek produk dengan setidaknya ID diperlukan.
                 * Kita bisa menggunakan nama/ harga dummy jika tidak diketahui dari peta GUI.
                 */
                productToProcess = new Product(productId, "Unknown (ID: "+productId+")", 0.0); //
            }
            
            stockManager.syncStock(productToProcess, -quantity); //
            
            final String finalProductName = productNameForDisplay;
            SwingUtilities.invokeLater(() -> {
                outputArea.append("Stock decreased for " + finalProductName + " by " + quantity + ".\n");
                outputArea.append("Current stock: " + stockManager.inventory.checkStock(productToProcess) + "\n"); //
                clearInputFields();
            });
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid numeric value for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            final String errorMessage = ex.getMessage();
            JOptionPane.showMessageDialog(frame, "An error occurred: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(() -> outputArea.append("Error: " + errorMessage + "\n"));
        }
    }

    private void handleCheckStock(ActionEvent e) {
        try {
            String productId = productIdField.getText().trim();
            if (productId.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a Product ID to check stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product productPlaceholder = new Product(productId, "", 0.0); //
            final int stock = stockManager.inventory.checkStock(productPlaceholder); //
            
            Product knownProduct = productsInGuiInventory.get(productId);
            String displayName = (knownProduct != null) ? knownProduct.getName() + " (ID: " + productId + ")" : "Product ID " + productId;

            SwingUtilities.invokeLater(() -> outputArea.append("Current stock for " + displayName + ": " + stock + "\n"));

        } catch (Exception ex) {
            final String errorMessage = ex.getMessage();
            JOptionPane.showMessageDialog(frame, "An error occurred: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(() -> outputArea.append("Error: " + errorMessage + "\n"));
        }
    }

    private void handleStartSimulation(ActionEvent e) {
        if (productsInGuiInventory.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No products available for simulation. Please add products using 'Add/Update Stock' first.", "Simulation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (simulationExecutor != null && !simulationExecutor.isTerminated()) {
            SwingUtilities.invokeLater(() -> outputArea.append("Simulation is already running or finishing.\n"));
            return;
        }
        simulationExecutor = Executors.newCachedThreadPool();
        Random random = new Random();

        List<Product> productListForSimulation = new ArrayList<>(productsInGuiInventory.values());

        SwingUtilities.invokeLater(() -> {
            outputArea.append("Starting buy/sell simulation using products added via GUI...\n");
            outputArea.append("Simulating on " + productListForSimulation.size() + " available product(s).\n");
        });

        new Thread(() -> {
            int counter = 0;
            int maxTasks = 20; 

            while (counter < maxTasks && !Thread.currentThread().isInterrupted() && !productListForSimulation.isEmpty()) {
                Product currentProduct = productListForSimulation.get(random.nextInt(productListForSimulation.size()));

                int operationInput = random.nextInt(2);
                String operationType = (operationInput == 0) ? "order" : "return"; 
                int quantity = random.nextInt(5) + 1;

                final String opTypeFinal = operationType;
                final int quantityFinal = quantity;
                final Product productFinal = currentProduct;

                simulationExecutor.submit(() -> {
                    String threadName = Thread.currentThread().getName();
                    String logMessage = String.format("[%s] Simulating: %s %d of %s (ID: %s)",
                                                    threadName, opTypeFinal, quantityFinal, productFinal.getName(), productFinal.getProductId());
                    SwingUtilities.invokeLater(() -> outputArea.append(logMessage + "\n"));

                    simulationExecutor.submit(new StockUpdater(opTypeFinal, productFinal, quantityFinal, stockManager)); 

                    int stockNow = stockManager.inventory.checkStock(productFinal); 
                    String stockUpdateMsg = String.format("[%s] Stock for %s (ID: %s) before async update completes: %d",
                                                          threadName, productFinal.getName(), productFinal.getProductId(), stockNow);
                    SwingUtilities.invokeLater(() -> outputArea.append(stockUpdateMsg + "\n"));
                });

                counter++;
                try {
                    Thread.sleep(300 + random.nextInt(400)); 
                } catch (InterruptedException ex) {
                    SwingUtilities.invokeLater(() -> outputArea.append("Simulation worker thread interrupted.\n"));
                    Thread.currentThread().interrupt();
                    break; 
                }
            }

            final int submittedTasks = counter;
            SwingUtilities.invokeLater(() -> outputArea.append("All " + submittedTasks + " simulation tasks submitted. Shutting down simulation executor...\n"));
            simulationExecutor.shutdown();
            try {
                if (!simulationExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                    simulationExecutor.shutdownNow();
                    SwingUtilities.invokeLater(() -> outputArea.append("Simulation executor did not terminate gracefully, forced shutdown.\n"));
                } else {
                    SwingUtilities.invokeLater(() -> outputArea.append("Simulation executor finished.\n"));
                }
            } catch (InterruptedException ie) {
                simulationExecutor.shutdownNow();
                Thread.currentThread().interrupt();
                SwingUtilities.invokeLater(() -> outputArea.append("Simulation executor shutdown interrupted.\n"));
            }
        }).start();
    }
}