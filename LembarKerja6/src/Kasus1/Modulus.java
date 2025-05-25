package Kasus1;

class Modulus extends Thread {
    private int dividend;
    private int divisor;
    private String taskName;
    
    public Modulus(int dividend, int divisor, String taskName) {
        this.dividend = dividend;
        this.divisor = divisor;
        this.taskName = taskName;
    }
    
    @Override
    public void run() {
        System.out.println(taskName + " (Modulus) dimulai oleh " + Thread.currentThread().getName());
        
        int result = dividend % divisor;
        System.out.println(taskName + " (Modulus) selesai: " + dividend + " % " + divisor + " = " + result + 
                          " (dieksekusi oleh " + Thread.currentThread().getName() + ")");
    }
}