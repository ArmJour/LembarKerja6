package Kasus2;

import java.util.*;

public class Inventory {
    private Map<String, Integer> stock = new HashMap<>();

    public int checkStock(Product product) {
        return stock.getOrDefault(product.getProductId(), 0);
    }

    public void updateStock(Product product, int quantity) {
        if (quantity < 0 && checkStock(product) + quantity < 0) {
            System.out.println("Insufficient stock to remove " + Math.abs(quantity) + " of " + product.getName());
        } else {
            stock.put(product.getProductId(), checkStock(product) + quantity);
            System.out.println("Updated stock for " + product.getName() + ": " + checkStock(product));
        }
    }

    public void addProduct(Product product, int quantity) {
        if (quantity < 0) {
            System.out.println("Cannot add negative quantity of " + product.getName());
        } else {
            updateStock(product, quantity);
        }
    }   

    public void removeProduct(Product product, int quantity) {
        if (quantity < 0) {
            System.out.println("Cannot remove negative quantity of " + product.getName());
        } else {
            updateStock(product, -quantity);
        }
    }
}
