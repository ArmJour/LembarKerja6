package Kasus1;

public class App {
    public static void main(String[] args) {
        System.out.println("=== Program Perhitungan Secara Paralel ===");
        System.out.println("Memulai 5 operasi secara bersamaan...\n");
        
        int[] numbers = {16, 25, 36, 49, 64};
        int[] divisors = {3, 4, 5, 6, 7};
        double[] prices = {100000, 250000, 350000, 480000, 650000};
        double[] discounts = {10, 15, 20, 25, 30};
        
        Thread[] allTasks = new Thread[25];
        int taskIndex = 0;
        
        System.out.println("--- Memulai semua tugas ---");
        
        for (int i = 0; i < numbers.length; i++) {
            allTasks[taskIndex] = new HitungPersegi(numbers[i], "Tugas-" + (i + 1));
            allTasks[taskIndex].start();
            taskIndex++;
        }
        
        for (int i = 0; i < numbers.length; i++) {
            allTasks[taskIndex] = new HitungKubik(numbers[i], "Tugas-" + (i + 1));
            allTasks[taskIndex].start();
            taskIndex++;
        }
        
        for (int i = 0; i < numbers.length; i++) {
            allTasks[taskIndex] = new AkarKuadrat(numbers[i], "Tugas-" + (i + 1));
            allTasks[taskIndex].start();
            taskIndex++;
        }
        
        for (int i = 0; i < numbers.length; i++) {
            allTasks[taskIndex] = new Modulus(numbers[i], divisors[i], "Tugas-" + (i + 1));
            allTasks[taskIndex].start();
            taskIndex++;
        }
        
        for (int i = 0; i < numbers.length; i++) {
            allTasks[taskIndex] = new Discount(prices[i], discounts[i], "Tugas-" + (i + 1));
            allTasks[taskIndex].start();
            taskIndex++;
        }
        
        try {
            for (Thread task : allTasks) {
                if (task != null) {
                    task.join();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread terinterupsi.");
            e.printStackTrace();
        }
        
        System.out.println("\n=== Semua tugas perhitungan selesai ===");
        System.out.println("Total thread yang dijalankan: " + taskIndex);
    }
}
