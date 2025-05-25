package Kasus2;

public class Order {
    private String orderId;
    private Product product;
    private int quantity;
    
    public Order(String orderId, Product product, int quantity){
        this.orderId=orderId;
        this.product=product;
        this.quantity=quantity;
    }
       
    public void processOrder() {
        System.out.println("Return processed: " + orderId);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
