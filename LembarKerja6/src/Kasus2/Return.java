package Kasus2;

public class Return {
    private String returnId;
    private Product product;
    private int quantity;
    
    public Return(String returnId, Product product, int quantity){
        this.returnId=returnId;
        this.product=product;
        this.quantity=quantity;
    }
    
    public void processReturn() {
        System.out.println("Return processed: " + returnId);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
    
}
