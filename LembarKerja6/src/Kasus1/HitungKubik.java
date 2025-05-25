package Kasus1;

public class HitungKubik extends Thread{
    private int number;
    private String taskName;
    
    public HitungKubik(int number, String taskName) {
        this.number = number;
        this.taskName = taskName;
    }
    
    @Override
    public void run() {
        System.out.println(taskName + " (Kubik) dimulai oleh " + Thread.currentThread().getName());
        
        int result = number * number * number;
        System.out.println(taskName + " (Kubik) selesai: " + number + "^3 = " + result + 
                          " (dieksekusi oleh " + Thread.currentThread().getName() + ")");
    }
}
