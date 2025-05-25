package Kasus2;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        // Create instances of Inventory and StockManager
        Inventory inventory = new Inventory(); //
        StockManager stockManager = new StockManager(inventory); //

        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Use the new CombinedGUIManager
                new GUIManager(stockManager);
            }
        });
    } 
}