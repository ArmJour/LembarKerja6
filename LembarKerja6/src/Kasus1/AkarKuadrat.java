package Kasus1;

public class AkarKuadrat extends Thread{
    private double number;
    private String taskName;
    
    public AkarKuadrat(double number, String taskName) {
        this.number = number;
        this.taskName = taskName;
    }
    
    @Override
    public void run() {
        System.out.println(taskName + " (Akar Kuadrat) dimulai oleh " + Thread.currentThread().getName());
        
        double result = Math.sqrt(number);
        System.out.println(taskName + " (Akar Kuadrat) selesai: \u221A" + number + " = " +
                          String.format("%.2f", result) + 
                          " (dieksekusi oleh " + Thread.currentThread().getName() + ")");
    }
}
