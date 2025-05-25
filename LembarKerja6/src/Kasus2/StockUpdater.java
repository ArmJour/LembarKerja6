package Kasus2;

public class StockUpdater extends Thread {
    private String operationType;
    private Product product;
    private int quantity;
    private StockManager stockManager;
    
    public StockUpdater(String operationType, Product product, int quantity, StockManager stockManager) {
        this.operationType = operationType;
        this.product = product;
        this.quantity = quantity;
        this.stockManager = stockManager;
    }
    
    public void run(){
         if (operationType.equalsIgnoreCase("order")) {
            Order order = new Order("ORD" + System.nanoTime(), product, quantity);
            stockManager.performOrderOperation(order);
        } else if (operationType.equalsIgnoreCase("return")) {
            Return ret = new Return("RET" + System.nanoTime(), product, quantity);
            stockManager.performReturnOperation(ret);
        }
    }
}
