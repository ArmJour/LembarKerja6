package Kasus2;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Use GUIManager 
                new GUIManager(new StockManager(new Inventory()));
            }
        });
    } 
}