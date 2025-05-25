package Kasus1;

public class HitungPersegi extends Thread {
    private int number;
    private String taskName;
    
    public HitungPersegi(int number, String taskName) {
        this.number = number;
        this.taskName = taskName;
    }
    
    @Override
    public void run() {
        System.out.println(taskName + " (Kuadrat) dimulai oleh " + Thread.currentThread().getName());
        
        int result = number * number;
        System.out.println(taskName + " (Kuadrat) selesai: " + number + "\u00B2 = " + result + 
                          " (dieksekusi oleh " + Thread.currentThread().getName() + ")");
    }
}
