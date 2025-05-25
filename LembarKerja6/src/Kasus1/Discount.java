package Kasus1;

class Discount extends Thread {
    private double originalPrice;
    private double discountPercent;
    private String taskName;
    
    public Discount(double originalPrice, double discountPercent, String taskName) {
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
        this.taskName = taskName;
    }
    
    @Override
    public void run() {
        System.out.println(taskName + " (Diskon) dimulai oleh " + Thread.currentThread().getName());
        
        double discountAmount = originalPrice * (discountPercent / 100);
        double finalPrice = originalPrice - discountAmount;
        
        System.out.println(taskName + " (Diskon) selesai: Rp" + String.format("%.0f", originalPrice) + 
                          " dengan diskon " + discountPercent + "% = Rp" + String.format("%.0f", finalPrice) + 
                          " (dieksekusi oleh " + Thread.currentThread().getName() + ")");
    }
}
