package Kasus2;

public class StockManager {
    protected Inventory inventory;
    
    public StockManager(Inventory inventory) {
        this.inventory = inventory;
    }

    
    public void syncStock(Product product, int quantity){
        synchronized (inventory) {
            inventory.updateStock(product, quantity);
        }
    }
    
    public synchronized void performReturnOperation(Return returnOrder){
        inventory.addProduct(returnOrder.getProduct(), returnOrder.getQuantity());
        returnOrder.processReturn();
    }
    
    public synchronized void performOrderOperation(Order order){
        inventory.removeProduct(order.getProduct(), order.getQuantity());
        order.processOrder();
    }
}
